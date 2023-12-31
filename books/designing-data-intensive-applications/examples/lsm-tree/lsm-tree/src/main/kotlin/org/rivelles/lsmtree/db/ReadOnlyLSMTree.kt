package org.rivelles.lsmtree.db

import java.math.BigInteger
import java.net.ServerSocket
import java.net.Socket

class ReadOnlyLSMTree(capacity: Int = 100, private val port: Int, private val leaderAddress: String): LSMTree {
    override val memTable = MemTable(capacity)
    override val wal = WriteAheadLog()
    override val serverSocket = ServerSocket(port)
    private val connectionLeader = Socket(leaderAddress.split(":")[0], leaderAddress.split(":")[1].toInt())

    var currentOffset: BigInteger = BigInteger.ZERO

    override fun runTCPServer(port: Int) {
        println("TCP server started and waiting for connection.")
        while(true) {
            val clientSocket = serverSocket.accept()
            val input = clientSocket.getInputStream().bufferedReader().readLine()
            val (key, value) = input.split(":")
            memTable.put(key, value)
            if (memTable.isFull()) {
                createNewSegment()
            }
            wal.write(key, value)
            currentOffset++
        }
    }

    private fun createNewSegment() {
        println("Memtable is full, creating new segment...")
        val nextSegment = getLastSegment() + 1
        memTable.flushToSegment(nextSegment)
        println("Segment created successfully!")
    }

    override fun readAndExecute(): Boolean {
        val nextCommand = readLine()!!.split(" ")

        val key = nextCommand[1]

        when (nextCommand[0]) {
            "get" -> println(get(key))
            "exit" -> return true
            else -> println("Invalid command")
        }
        return false
    }

    override fun initialize() {
        super.initialize()
        val outputStream = connectionLeader.getOutputStream()

        outputStream.writer().write("follower:localhost:$port:$currentOffset\n")
    }
}