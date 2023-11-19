fun main() {
    println("Set initial capacity of memtable: ")
    val capacity = readLine()!!.toInt()
    println("Database is starting...")
    val lsmTree = LSMTree(capacity)

    var isExited = false
    while (!isExited) {
        val nextCommand = readLine()!!.split(" ")

        when (nextCommand[0]) {
            "put" -> lsmTree.put(nextCommand[1], nextCommand[2])
            "get" -> println(lsmTree.get(nextCommand[1]))
            "exit" -> isExited = true
        }
    }
}