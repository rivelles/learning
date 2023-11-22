package org.rivelles.lsmtree

import java.io.File

class WriteAheadLog {
    private var currentLog = 0
    private val file = File("wal/wal.log")

    fun initialize() {
        val dir = File("wal")

        if (!dir.exists()) {
            dir.mkdir()
        }
        if (!file.exists()) {
            file.createNewFile()
        }
        currentLog = file.readLines().size
    }

    fun write(key: String, value: String) {
        val writer = file.outputStream().writer()
        writer.appendLine("$key:$value")
        writer.close()

        currentLog++
    }
}