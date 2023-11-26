package org.rivelles.lsmtree.db

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