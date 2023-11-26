package org.rivelles.lsmtree

import java.net.ServerSocket

open class ReadWriteLSMTree(capacity: Int = 100, port: Int): LSMTree {
    override val memTable = MemTable(capacity)
    override val wal = WriteAheadLog()
    private lateinit var replicasAddresses: MutableList<String>
    override val serverSocket = ServerSocket(port)

    override fun put(key: String, value: String) {
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

    override fun runTCPServer(port: Int)  {
        println("TCP server started and waiting for connection.")
        while(true) {
            val clientSocket = serverSocket.accept()
            val input = clientSocket.getInputStream().bufferedReader().readLine()
            replicasAddresses.add(input)
        }
    }
}