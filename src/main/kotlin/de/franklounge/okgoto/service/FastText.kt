package de.franklounge.okgoto.service

import com.github.jelmerk.knn.DistanceFunctions
import com.github.jelmerk.knn.SearchResult
import com.github.jelmerk.knn.hnsw.HnswIndex
import com.github.jelmerk.knn.util.VectorUtils.normalize
import java.io.*
import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import java.util.zip.GZIPInputStream

/**
 * Example application that downloads the english fast-text word vectors, inserts them into an hnsw index and lets
 * you query them.
 */
object FastText {
    // private const val WORDS_FILE_URL = "https://dl.fbaipublicfiles.com/fasttext/vectors-crawl/cc.en.300.vec.gz"
    private val TMP_PATH = Paths.get(System.getProperty("java.io.tmpdir"))

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val file = File("data/de.vec.gz").toPath()
        /*
        val file = TMP_PATH.resolve("cc.en.300.vec.gz")
        if (!Files.exists(file)) {
            downloadFile(WORDS_FILE_URL, file)
        } else {
            System.out.printf("Input file already downloaded. Using %s%n", file)
        }
        */
        val words: List<Word> = loadWordVectors(file)
        println("Constructing index.")
        val hnswIndex: HnswIndex<String, FloatArray, Word, Float> = HnswIndex
            .newBuilder(300, DistanceFunctions.FLOAT_INNER_PRODUCT, words.size)
            .withM(16)
            .withEf(200)
            .withEfConstruction(200)
            .build()
        val start = System.currentTimeMillis()
        hnswIndex.addAll(words) { workDone: Int, max: Int ->
            System.out.printf("Added %d out of %d words to the index.%n", workDone, max)
        }
        val end = System.currentTimeMillis()
        val duration = end - start
        System.out.printf(
            "Creating index with %d words took %d millis which is %d minutes.%n",
            hnswIndex.size(),
            duration,
            TimeUnit.MILLISECONDS.toMinutes(duration)
        )
        val console = LineNumberReader(InputStreamReader(System.`in`))
        val k = 10
        while (true) {
            println("Enter a word : ")
            val input = console.readLine()
            val words = input.split(" ")
            val vecs = words.mapNotNull { hnswIndex[it].orElse(null) }
            println("Matched ${vecs.map { it.id() }}")
            val sum = vecs.map { it.vector() }.reduce { a, b -> a.addEach(b) }
            val norm = normalize(sum)

            val approximateResults: List<SearchResult<Word, Float>> = hnswIndex.findNearest(norm, k)
            println("Most similar words found using HNSW index : %n%n")
            for (result in approximateResults) {
                val diffVec = normalize(norm.subEach(result.item().vector()))
                val diffs = hnswIndex.findNearest(diffVec, words.size+1)
                    .map { it.item().id() }.filter { !words.contains(it) }
                println("${result.item().id()} ${diffs} ${result.distance()}")
            }
        }
    }

    @Throws(IOException::class)
    private fun downloadFile(url: String, path: Path) {
        System.out.printf("Downloading %s to %s. This may take a while.%n", url, path)
        URL(url).openStream().use { `in` -> Files.copy(`in`, path) }
    }

    @Throws(IOException::class)
    private fun loadWordVectors(path: Path): List<Word> {
        System.out.printf("Loading words from %s%n", path)
        BufferedReader(
            InputStreamReader(
                GZIPInputStream(Files.newInputStream(path)),
                StandardCharsets.UTF_8
            )
        ).use { reader ->
            return reader.lines()
                .skip(1)
                .map { line: String ->
                    val tokens = line.split(" ").toTypedArray()
                    val word = tokens[0]
                    val vector = FloatArray(tokens.size - 1)
                    for (i in 1 until tokens.size - 1) {
                        vector[i] = tokens[i].toFloat()
                    }
                    Word(word, normalize(vector)) // normalize the vector so we can do inner product search
                }
                .collect(Collectors.toList())
        }
    }
}

fun FloatArray.addEach(b: FloatArray) = FloatArray(this.size).also {
    for (i in it.indices) {
        it[i] = this[i] + b[i]
    }
}

fun FloatArray.subEach(b: FloatArray) = FloatArray(this.size).also {
    for (i in it.indices) {
        it[i] = this[i] - b[i]
    }
}
