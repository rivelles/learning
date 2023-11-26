package org.rivelles.lsmtree.db

import java.net.ServerSocket

internal class ReadWriteLSMTree(capacity: Int = 100, port: Int): LSMTree {
    override val memTable = MemTable(capacity)
    override val wal = WriteAheadLog()
    private lateinit var replicasAddresses: MutableList<String>
    override val serverSocket = ServerSocket(port)

    fun put(key: String, value: String) {
        write(key, value)
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