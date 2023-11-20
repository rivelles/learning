package serialization

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

sealed interface DataSerializer {
    fun serialize(data: Any): ByteArray
    fun deserialize(data: ByteArray): Any

    object JSONSerializer : DataSerializer {
        private val gson = Gson()

        override fun serialize(data: Any): ByteArray {
            return gson.toJson(data, data.javaClass).encodeToByteArray()
        }

        override fun deserialize(data: ByteArray): Any {
            val type = object : TypeToken<Map<Long, Any>>() {}.type
            return gson.fromJson(data.decodeToString(), type)
        }
    }
}