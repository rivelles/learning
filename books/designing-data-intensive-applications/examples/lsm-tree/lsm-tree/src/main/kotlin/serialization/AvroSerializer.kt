package serialization

import org.apache.avro.Schema
import org.apache.avro.file.DataFileReader
import org.apache.avro.file.DataFileWriter
import org.apache.avro.generic.GenericData
import java.io.File

class AvroSerializer {
    companion object {
        val schema: Schema = Schema.Parser().parse("""
            {
                "type": "record",
                "namespace": "org.rivelles",
                "name": "Segments",
                "fields": [
                    {
                        "name": "Segment",
                        "type": {
                            "type": "map",
                            "values": "string"
                        }
                    }
                ]
            }
        """.trimIndent())
    }
    fun write(obj: Map<String, String>, segmentID: String) {
        val record = GenericData.Record(schema)
        record.put("Segment", obj)

        val datumWriter = GenericData.get().createDatumWriter(schema)
        val dataFileWriter = DataFileWriter(datumWriter)
        val file = File("segments/%s.bin".format(segmentID))
        file.createNewFile()

        dataFileWriter.create(schema, file)
        dataFileWriter.append(record)
        dataFileWriter.close()
    }

    fun read(segmentID: String): Map<String, String> {
        val datumReader = GenericData.get().createDatumReader(schema)
        val datumFileReader = DataFileReader(File("segments/%s.bin".format(segmentID)), datumReader)

        val genericRecord = datumFileReader.next() as GenericData.Record
        val segment = genericRecord.get("Segment") as Map<CharSequence, CharSequence>
        return segment.asSequence().map { it.key.toString() to it.value.toString() }.toMap()
    }
}