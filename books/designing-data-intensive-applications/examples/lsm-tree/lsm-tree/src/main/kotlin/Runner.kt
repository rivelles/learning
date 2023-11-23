import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.rivelles.lsmtree.LSMTree

fun main() = runBlocking {
    println("Set initial capacity of memtable: ")
    val capacity = readLine()!!.toInt()
    println("Set mode (RW or R): ")
    val mode = readLine()!!
    println("Select port: ")
    val port = readLine()!!.toInt()
    println("Database is starting...")
    val lsmTree = LSMTree(capacity, mode)
    lsmTree.initialize()
    launch(Dispatchers.IO) {
        lsmTree.runTCPServer(port)
    }
    println("Database running!")

    var isExited = false
    while (!isExited) {
        val nextCommand = readLine()!!.split(" ")

        val key = nextCommand[1]

        when (nextCommand[0]) {
            "put" -> lsmTree.put(key, nextCommand[2])
            "get" -> println(lsmTree.get(key))
            "exit" -> isExited = true
        }
    }
}