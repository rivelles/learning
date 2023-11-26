package app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.rivelles.lsmtree.db.ReadOnlyLSMTree
import org.rivelles.lsmtree.db.ReadWriteLSMTree

fun main() = runBlocking {
    println("Set initial capacity of memtable: ")
    val capacity = readLine()!!.toInt()
    println("Set mode (RW or R): ")
    val mode = readLine()!!
    println("Select port: ")
    val port = readLine()!!.toInt()
    println("Database is starting...")
    val lsmTree = mode.let {
        when (it) {
            "RW" -> ReadWriteLSMTree(capacity, port)
            "R" -> {
                println("Enter leader address: ")
                val leaderAddress = readLine()!!
                ReadOnlyLSMTree(capacity, port, leaderAddress)
            }
            else -> throw Exception("Invalid mode")
        }
    }
    lsmTree.initialize()
    launch(Dispatchers.IO) {
        lsmTree.runTCPServer(port)
    }
    println("Database running!")

    var isExited = false
    while (!isExited) {
        isExited = lsmTree.readAndExecute()
    }
}