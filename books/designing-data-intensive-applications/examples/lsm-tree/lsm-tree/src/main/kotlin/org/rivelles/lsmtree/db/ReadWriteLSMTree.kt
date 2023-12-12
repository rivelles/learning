package org.rivelles.lsmtree.db

import java.net.ServerSocket

internal class ReadWriteLSMTree(capacity: Int = 100, port: Int): LSMTree {
    override val memTable = MemTable(capacity)
    override val wal = WriteAheadLog()
    private lateinit var replicasAddresses: MutableList<String>
    override val serverSocket = ServerSocket(port)

    private fun put(key: String, value: String) {
        runLoggingTime("Put") {
            memTable.put(key, value)
            if (memTable.isFull()) {
                println("Memtable is full, creating new segment...")
                val nextSegment = getLastSegment() + 1
                memTable.flushToSegment(nextSegment)
                println("Segment created successfully!")
            }
            wal.write(key, value)
        }
    }

    override fun runTCPServer(port: Int)  {
        println("TCP server started and waiting for connection.")
        while(true) {
            val clientSocket = serverSocket.accept()
            val input = clientSocket.getInputStream().bufferedReader().readLine()
            if (input.startsWith("follower")) {
                addReplica(input)
            }
        }
    }

    private fun addReplica(input: String) {
        val (address, followerPort, offset) = input.split(":")
        println("Follower $address:$followerPort connected with offset $offset")
        replicasAddresses.add("$address:$followerPort")
    }

    override fun readAndExecute(): Boolean {
        val nextCommand = readLine()!!.split(" ")

        val key = nextCommand[1]

        when (nextCommand[0]) {
            "put" -> put(key, nextCommand[2])
            "get" -> println(get(key))
            "exit" -> return true
            else -> println("Invalid command")
        }
        return false
    }
}