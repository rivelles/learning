package org.rivelles.lsmtree.db

class MemTable(private val capacity: Int = 100) {
    private val map = mutableMapOf<String, String>()

    fun get(key: String): Any? = map[key]

    fun put(key: String, value: String) {
        map[key] = value
    }

    fun isFull() = map.size >= capacity

    fun flushToSegment(nextSegment: Long) {
        Segment.flush(nextSegment.toString(), map)
        map.clear()
    }
}