package de.franklounge.okgoto.service

import com.github.jelmerk.knn.DistanceFunctions
import com.github.jelmerk.knn.SearchResult
import com.github.jelmerk.knn.hnsw.HnswIndex
import com.github.jelmerk.knn.util.VectorUtils.normalize
import org.apache.commons.lang3.SerializationUtils
import java.io.*
import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.zip.GZIPInputStream

/**
 * Example application that downloads the english fast-text word vectors, inserts them into an hnsw index and lets
 * you query them.
 */
object FastText {
    private val TMP_PATH = Paths.get(System.getProperty("java.io.tmpdir"))

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val hnswIndex: HnswIndex<String, FloatArray, Word, Float> = getOrCreateWordIndex()

//               for (i in 0..299) {
//                   val v = FloatArray(300); v[i] = 1.0f; println("$i: ${hnswIndex.findNearest(v, 5).map { it.item().id() }}")
//               }

//        val console = LineNumberReader(InputStreamReader(System.`in`))
        val console = LineNumberReader(InputStreamReader(FileInputStream("data/hobbies_de.txt")))
        val k = 1
        while (true) {
            // println("Enter a word : ")
            val input = console.readLine()
            if (null == input) break
            val words = input.split("\\s+".toRegex())
            val vecs = words.mapNotNull { hnswIndex[it].orElse(null) }
            if (vecs.isEmpty()) {
                // println("not matched $words")
                continue
            }
            // println("Matched ${vecs.map { it.id() }}")
            val sum = vecs.map { it.vector() }.reduce { a, b -> a.addEach(b) }
            val norm = normalize(sum)

            val approximateResults: List<SearchResult<Word, Float>> = hnswIndex.findNearest(norm, k)
            println("$words -> ${approximateResults[0].item().id()}")
            /*
            for (result in approximateResults) {
                val diffVec = normalize(norm.subEach(result.item().vector()))
                val diffs = hnswIndex.findNearest(diffVec, words.size + 1)
                    .map { it.item().id() }.filter { word -> !words.contains(word) }
                println("$words -> ${result.item().id()} $diffs ${result.distance()}")
            }

             */
        }
    }

    @Throws(IOException::class)
    private fun downloadFile(url: String, path: Path) {
        System.out.printf("Downloading %s to %s. This may take a while.%n", url, path)
        URL(url).openStream().use { `in` -> Files.copy(`in`, path) }
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

fun buildWordToVec(): HnswIndex<String, FloatArray, Word, Float> {
    val file = File("data/de.vec.gz").toPath()
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
    /*
    val end = System.currentTimeMillis()
    val duration = end - start
    System.out.printf(
        "Creating index with %d words took %d millis which is %d minutes.%n",
        hnswIndex.size(),
        duration,
        TimeUnit.MILLISECONDS.toMinutes(duration)
    )
    */
    return hnswIndex
}

private fun loadWordVectors(path: Path): List<Word> {
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

fun getOrCreateWordIndex(): HnswIndex<String, FloatArray, Word, Float> {
    val wordsFile = File("target/words.index.serialized")
    val wordIndex =
        if (wordsFile.exists()) {
            SerializationUtils.deserialize(FileInputStream(wordsFile)) as HnswIndex<String, FloatArray, Word, Float>
        } else {
            buildWordToVec().also {
                SerializationUtils.serialize(it, FileOutputStream(wordsFile))
            }
        }
    return wordIndex
}
