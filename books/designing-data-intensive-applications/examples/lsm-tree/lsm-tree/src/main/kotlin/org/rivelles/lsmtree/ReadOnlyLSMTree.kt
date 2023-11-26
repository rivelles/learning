package org.rivelles.lsmtree
//
//import java.net.ServerSocket
//
//class ReadOnlyLSMTree: LSMTree() {
//
//    override fun put(key: String, value: String) {
//        throw UnsupportedOperationException("This is a read only database")
//    }
//
//    override fun runTCPServer(port: Int)  {
//        serverSocket = ServerSocket(port)
//        println("TCP server started and waiting for connection.")
//        while(true) {
//            val clientSocket = serverSocket.accept()
//            val input = clientSocket.getInputStream().bufferedReader().readLine()
//            replicasAddresses.add(input)
//        }
//    }
//}