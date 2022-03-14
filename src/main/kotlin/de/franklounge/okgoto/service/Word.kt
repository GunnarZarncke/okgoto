package de.franklounge.okgoto.service

import com.github.jelmerk.knn.Item
import java.util.*

class Word(private val id: String, private val vector: FloatArray) :
    Item<String, FloatArray> {
    override fun id(): String {
        return id
    }

    override fun vector(): FloatArray {
        return vector
    }

    override fun dimensions(): Int {
        return vector.size
    }

    override fun toString(): String {
        return "Word{" +
            "id='" + id + '\'' +
            ", vector=" + Arrays.toString(vector) +
            '}'
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
