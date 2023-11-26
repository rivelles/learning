package org.rivelles.lsmtree

import java.net.ServerSocket

class ReadOnlyLSMTree(capacity: Int = 100, port: Int, val leaderAddress: String): LSMTree {
    override val memTable = MemTable(capacity)
    override val wal = WriteAheadLog()
    override val serverSocket = ServerSocket(port)

    override fun runTCPServer(port: Int)  {
        println("TCP server started and waiting for connection.")
        while(true) {
            val clientSocket = serverSocket.accept()
            val input = clientSocket.getInputStream().bufferedReader().readLine()
        }
    }
}