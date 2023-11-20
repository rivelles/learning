import serialization.AvroSerializer
import java.io.File
import java.io.OutputStreamWriter

/**
 * A simple implementation of a Log-Structured Merge Tree. It's only supposed to be used as a learning tool, as it doesn't
 * have any optimization and uses a map as the in-memory table so we don't need to worry about search algorithms
 * and how to store low-level data in memory.
 */
class LSMTree(capacity: Int = 100) {
    private val memTable = MemTable(capacity)
    private val wal = WriteAheadLog()

    init {
        val dir = File("segments")
        if (!dir.exists()) dir.mkdir()
        wal.initialize()
    }

    private fun runLoggingTime(operation: String, function: () -> Any?): Any? {
        val startTime = System.currentTimeMillis()
        val returnedValue = function()
        val endTime = System.currentTimeMillis()
        println("$operation executed. Time elapsed: ${endTime - startTime} ms")

        return returnedValue
    }

    fun get(key: String): Any? {
        return runLoggingTime("Get") {
            memTable.get(key) ?: run {
                var segmentIteratorIndex = getLastSegment()
                while (segmentIteratorIndex > 0) {
                    val segment = Segment(segmentIteratorIndex.toString())

                    segment.let {
                        segment.get(key)?.let { return@runLoggingTime it }
                    } ?: segmentIteratorIndex--
                }
                return@runLoggingTime null
            }
        }
    }

    fun put(key: String, value: String) {
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

    private fun getLastSegment(): Long {
        val file = File("segments")
        if (file.list().isEmpty()) return 0

        var biggestSegment = 0L
        file.list().forEach {
            val segmentNumber = it.split(".")[0].toLong()
            if (segmentNumber > biggestSegment) biggestSegment = segmentNumber
        }
        return biggestSegment
    }

    class MemTable(private val capacity: Int = 100) {
        private val map = mutableMapOf<String, String>()

        fun get(key: String): Any? = map[key]

        fun put(key: String, value: String) {
            map[key] = value
        }

        fun isFull() = map.size >= capacity

        fun clear() = map.clear()

        fun createSegment(nextSegment: Long) = Segment(nextSegment.toString(), map)
    }

    class Segment {

        private var content: Map<String, String>? = null
        private val segmentID: String
        private val serializer = AvroSerializer()

        constructor(segmentID: String, content: Map<String, String>) {
            this.segmentID = segmentID
            this.content = content

            serializer.write(content, segmentID)
        }

        constructor(fileDir: String) {
            this.segmentID = fileDir
            this.content = serializer.read(fileDir)
        }

        fun get(key: String): String? {
            return content?.get(key)
        }
    }

    class WriteAheadLog {
        private var currentLog = 0
        private val file = File("wal/wal.log")

        fun initialize() {
            val dir = File("wal")

            if (!dir.exists()) {
                dir.mkdir()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            currentLog = file.readLines().size
        }

        fun write(key: String, value: String) {
            val writer = file.outputStream().writer()
            writer.appendLine("$key:$value")
            writer.close()

            currentLog++
        }
    }
}