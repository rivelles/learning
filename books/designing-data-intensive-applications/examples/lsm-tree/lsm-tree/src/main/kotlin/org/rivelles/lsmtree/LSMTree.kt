package org.rivelles.lsmtree

import java.io.File
import java.net.ServerSocket

/**
 * A simple implementation of a Log-Structured Merge Tree. It's only supposed to be used as a learning tool, as it doesn't
 * have any optimization and uses a map as the in-memory table so we don't need to worry about search algorithms
 * and how to store low-level data in memory.
 */
class LSMTree(capacity: Int = 100) {
    private val memTable = MemTable(capacity)
    private val wal = WriteAheadLog()
    private lateinit var serverSocket: ServerSocket

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

    fun put(key: String, value: String) {
        runLoggingTime("Put") {
            memTable.put(key, value)
            if (memTable.isFull()) {
                println("Memtable is full, creating new segment...")
                val nextSegment = getLastSegment() + 1
                memTable.createSegment(nextSegment)
                memTable.clear()
                println("Segment created successfully!")
            }
            wal.write(key, value)
        }
    }

    fun startTCPServer(port: Int)  {
        serverSocket = ServerSocket(port)
        println("Server started and waiting for connection.")
        val clientSocket = serverSocket.accept()
        val input = clientSocket.getInputStream().bufferedReader().readLine()
        println(input)
    }

    private fun runLoggingTime(operation: String, function: () -> Any?): Any? {
        val startTime = System.currentTimeMillis()
        val returnedValue = function()
        val endTime = System.currentTimeMillis()
        println("$operation executed. Time elapsed: ${endTime - startTime} ms")

        return returnedValue
    }

    private fun getLastSegment(): Long {
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