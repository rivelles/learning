fun main() {
    println("Set initial capacity of memtable: ")
    val capacity = readLine()!!.toInt()
    println("Database is starting...")
    val lsmTree = LSMTree(capacity)

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