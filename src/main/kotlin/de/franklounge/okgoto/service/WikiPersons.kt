package de.franklounge.okgoto.service

import com.github.jelmerk.knn.DistanceFunctions
import com.github.jelmerk.knn.SearchResult
import com.github.jelmerk.knn.hnsw.HnswIndex
import com.github.jelmerk.knn.util.VectorUtils
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.SerializationUtils
import org.apache.commons.lang3.StringEscapeUtils
import org.apache.commons.lang3.StringUtils
import java.io.*
import java.nio.file.Paths
import java.util.zip.ZipInputStream
import kotlin.math.min

/**
 * Example application that downloads the english fast-text word vectors, inserts them into an hnsw index and lets
 * you query them.
 */
object WikiPersons {
    // private const val WORDS_FILE_URL = "https://dl.fbaipublicfiles.com/fasttext/vectors-crawl/cc.en.300.vec.gz"
    private val TMP_PATH = Paths.get(System.getProperty("java.io.tmpdir"))

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val wordIndex = getOrCreateWordIndex()

        val hnswIndex = getOrCreatePersonIndex(wordIndex)

        val console = LineNumberReader(InputStreamReader(System.`in`))
        val k = 10
        while (true) {
            println("Enter words : ")
            val input = console.readLine()
            val words = input.split(" ")
            val vecs = words.mapNotNull { wordIndex[it].orElse(null) }
            println("Matched ${vecs.map { it.id() }}")
            val sum = vecs.map { it.vector() }.reduce { a, b -> a.addEach(b) }
            val norm = VectorUtils.normalize(sum)

            val approximateResults: List<SearchResult<Word, Float>> = hnswIndex.findNearest(norm, k)
            println("Most similar persons found using HNSW index")
            for (result in approximateResults) {
                println("${result.item().id()} ${result.distance()}")
            }
        }
    }

    private fun getOrCreatePersonIndex(wordIndex: HnswIndex<String, FloatArray, Word, Float>): HnswIndex<String, FloatArray, Word, Float> {
        val personFile = File("target/persons.index.serialized")
        val personIndex =
            if (personFile.exists()) {
                SerializationUtils.deserialize(FileInputStream(personFile)) as HnswIndex<String, FloatArray, Word, Float>
            } else {
                buildPersonsToVec(wordIndex).also {
                    SerializationUtils.serialize(it, FileOutputStream(personFile))
                }
            }
        return personIndex
    }

    fun buildPersonsToVec(wordIndex: HnswIndex<String, FloatArray, Word, Float>): HnswIndex<String, FloatArray, Word, Float> {
        val hnswIndex: HnswIndex<String, FloatArray, Word, Float> = HnswIndex
            .newBuilder(300, DistanceFunctions.FLOAT_INNER_PRODUCT, 300000)
            .withM(16)
            .withEf(200)
            .withEfConstruction(200)
            .build()

        forAllWikiPersons({ name, words ->
            val wordSet = words.take(100).toSet()
            val vecs = wordSet.mapNotNull { wordIndex[it].orElse(null) }
            if (!vecs.isEmpty()) {
                val sum = vecs.map { it.vector() }.reduce { a, b -> a.addEach(b) }
                val norm = VectorUtils.normalize(sum)

                val nearestWord = wordIndex.findNearest(norm, 1).firstOrNull()
                println("nearest to $name is ${nearestWord?.item()?.id()}")

                hnswIndex.add(Word(name, norm))
                // println("${entry.name}: \"${res.shorten(300).replace("\n", "\\n")}...\"")
            }
        })

        return hnswIndex
    }
}

fun String.shorten(n: Int) = this.substring(0, min(n, this.length))
fun String.between(prefix: String, suffix: String) = StringUtils.substringBetween(this, prefix, suffix)

fun clean(wiki: String): String {
    var text = wiki

    while (true) {
        val before = text.length
        text = text
            .replace("\\[[^\\[\\]]*\\|([^\\[\\]]*)\\]".toRegex(), " \$1 ")
            .replace("\\[([^\\|\\[\\]]*)\\]".toRegex(), " \$1 ")
            .replace("\\{[^\\{\\}]*\\}".toRegex(), " ")
            .replace("<[^<>]*>".toRegex(), " ")
        if (text.length == before) break
    }
    text = text
        .replace("https?:[^\\s)\\]\\}\\)]*".toRegex(), " ")
        .replace("\\s\\s+".toRegex(), " ")
    return text
}

fun forAllWikiPersons(processTokens: (name: String, listOfTokens: List<String>) -> Unit) {
    val file = File("data/persons.zip")
    ZipInputStream(FileInputStream(file)).use {
        var n = 0
        while (n++ < 30000) {
            val entry = it.nextEntry
            if (entry.isDirectory) continue
            if (null == entry) break
            val words = wikiToTokens(it)
            processTokens(StringUtils.substringAfter(entry.name, "/"), words)
        }
    }
}

private fun wikiToTokens(it: ZipInputStream): List<String> {
    val wiki = IOUtils.toString(it, Charsets.UTF_8)
    val html = StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeHtml4(wiki))
    val entityRegex = "&[a-zA-Z#]+;".toRegex()
    if (html.contains(entityRegex)) {
        val m = entityRegex.toPattern().matcher(html)
        m.find()
        println("\n\n\nWARN illegal entity ${m.group()} at ${m.regionStart()} found in $html")
    }
    val text = clean(html)
    return text.split("[^0-9\\p{L}\\-]+".toRegex())
}
