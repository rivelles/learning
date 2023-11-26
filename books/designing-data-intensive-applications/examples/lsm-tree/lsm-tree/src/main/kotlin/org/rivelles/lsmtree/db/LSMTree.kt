package org.rivelles.lsmtree.db

import java.io.File
import java.net.ServerSocket

interface LSMTree {
    val memTable: MemTable
    val wal: WriteAheadLog
    val serverSocket: ServerSocket

    fun initialize() {
        println("Creating segments directory...")
        val dir = File("segments")
        if (!dir.exists()) dir.mkdir()
        wal.initialize()
    }

    fun get(key: String): Any? {
        return runLoggingTime("Get") {
            memTable.get(key) ?: run {
                var segmentIteratorIndex = getLastSegment()
                while (segmentIteratorIndex > 0) {
                    val segment = Segment(segmentIteratorIndex.toString())

                    segment.let {
                        segment.get(key)?.let { return@runLoggingTime it }
                    } ?: segmentIteratorIndex--
                }
                return@runLoggingTime null
            }
        }
    }

    fun runTCPServer(port: Int)
    fun readAndExecute(): Boolean

    fun runLoggingTime(operation: String, function: () -> Any?): Any? {
        val startTime = System.currentTimeMillis()
        val returnedValue = function()
        val endTime = System.currentTimeMillis()
        println("$operation executed. Time elapsed: ${endTime - startTime} ms")

        return returnedValue
    }

    fun getLastSegment(): Long {
        val file = File("segments")
        if (file.list().isEmpty()) return 0

        var biggestSegment = 0L
        file.list().forEach {
            val segmentNumber = it.split(".")[0].toLong()
            if (segmentNumber > biggestSegment) biggestSegment = segmentNumber
        }
        return biggestSegment
    }
}