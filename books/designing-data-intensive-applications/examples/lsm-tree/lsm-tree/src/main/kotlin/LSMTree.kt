import com.google.gson.Gson
import java.io.File

class LSMTree(capacity: Int = 100) {
    private val memTable = MemTable(capacity)

    init {
        val dir = File("segments")
        if (!dir.exists()) dir.mkdir()
    }

    fun get(key: Long): Any? {
        return memTable.get(key) ?: run {
            var segmentIteratorIndex = getLastSegment()
            while (segmentIteratorIndex > 0) {
                val segment = Segment(segmentIteratorIndex.toString())

                segment.let {
                    segment.get(key)?.let { return it }
                } ?: segmentIteratorIndex--
            }
            return null
        }
    }

    fun put(key: Long, value: String) {
        memTable.put(key, value)
        if (memTable.isFull()) {
            println("Memtable is full, creating new segment...")
            val nextSegment = getLastSegment() + 1
            memTable.createSegment(nextSegment)
            memTable.clear()
        }
    }

    private fun getLastSegment(): Long {
        val file = File("segments")
        if (file.list().isEmpty()) return 0

        var biggestSegment = 0L
        file.list().forEach {
            if (it.toLong() > biggestSegment) biggestSegment = it.toLong()
        }
        return biggestSegment
    }

    class MemTable(private val capacity: Int = 100) {
        private val map = mutableMapOf<Long, Any>()

        fun get(key: Long): Any? {
            return map[key]
        }

        fun put(key: Long, value: Any) {
            map[key] = value
        }

        fun isFull(): Boolean {
            return map.size >= capacity
        }

        fun clear() {
            map.clear()
        }

        fun createSegment(nextSegment: Long) = Segment(nextSegment.toString(), map)
    }

    class Segment {

        private var content: Map<Long, Any>? = null
        private val fileDir: String
        private val gson = Gson()

        constructor(fileDir: String, content: Map<Long, Any>?) {
            this.fileDir = fileDir
            this.content = content

            val file = File("segments/$fileDir")
            file.createNewFile()
            file.writeText(gson.toJson(content))
        }

        constructor(fileDir: String) {
            this.fileDir = fileDir

            val file = File("segments/$fileDir")
            file.takeIf { it.exists() }?.let {
                content = gson.fromJson(it.readText(), Map::class.java) as Map<Long, Any>?
            }
        }

        fun get(key: Long): Any? {
            return content?.get(key)
        }
    }
}