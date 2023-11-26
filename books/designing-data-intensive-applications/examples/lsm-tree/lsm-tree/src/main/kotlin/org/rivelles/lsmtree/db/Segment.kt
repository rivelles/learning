package org.rivelles.lsmtree.db

import org.rivelles.lsmtree.serialization.AvroSerializer

class Segment {

    private var content: Map<String, String>? = null
    private val segmentID: String
    private val serializer = AvroSerializer()

    private constructor(segmentID: String, content: Map<String, String>) {
        this.segmentID = segmentID
        this.content = content

        serializer.write(content, segmentID)
    }

    companion object {
        fun flush(segmentID: String, content: Map<String, String>) { Segment(segmentID, content) }
    }

    constructor(fileDir: String) {
        this.segmentID = fileDir
        this.content = serializer.read(fileDir)
    }

    fun get(key: String): String? {
        return content?.get(key)
    }
}