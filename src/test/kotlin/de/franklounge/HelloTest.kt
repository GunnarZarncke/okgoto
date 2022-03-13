package de.franklounge

import kotlin.test.assertEquals

class HelloTest {
    fun test() {
        assertEquals(1, 1)
/*
        val text =
            org.apache.commons.io.IOUtils.toString(java.io.FileInputStream("target/Arthur Schopenhauer"), "UTF-8")
        val res = "\\p{L}+".toRegex().findAll(text).map { it.value }.toSet()
        print("$res")
        */
    }
}
