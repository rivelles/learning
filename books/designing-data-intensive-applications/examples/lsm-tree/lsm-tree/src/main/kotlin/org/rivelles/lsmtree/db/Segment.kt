package org.rivelles.lsmtree.db

import org.rivelles.lsmtree.serialization.AvroSerializer

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