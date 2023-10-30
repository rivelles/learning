# Chapter 4: Encoding and Evolution

In order for systems to communicate, they need to agree on a data format and its evolution.
Data produced and read by different versions of applications must be compatible. We need to mantain compatibility
in both directions:

- **Backward compatibility**: new code can read data produced by old code.
- **Forward compatibility**: old code can read data produced by new code.

## Formats for Encoding Data

Usually, applications keep data in memory as objects. When you want to send data over the network or write it to disk,
you need to encode it as some kind of self-contained sequence of bytes (e.g. JSON, XML, CSV, etc). This is called
**serialization**, **encoding** or **marshalling**, and the opposite is called **deserialization**, **decoding** or 
**unmarshalling**.

Most languages have libraries for encoding data in various formats. Some of them are:
- java.io.Serializable
- Marshal (Ruby)
- pickle (Python)

These are very convenient when you are working only with your current language. But this is very limiting when you
want to communicate with another applications. So, in general it's not a good idea to use them outside their own
context.

### JSON, XML and Binary Variants

JSON, XML and CSV are popular formats for encoding data as text. They are human-readable and have libraries in most
languages. However, they also have some cons:
- There could be ambiguity in some data formats, such as numbers, dates and times.
- Usually, there is no schema associated with the data, so you have to infer the structure by yourself.

Despite these cons, JSON and XML are still very popular and are widely used in different types of applications.

#### Binary encoding

Binary encoding is more efficient than text encoding, because it is more compact and can be parsed more efficiently.
There are some libraries that can encode JSON or XML into a binary format, such as BSON, BJSON, MessagePack and WBXML.

For example, this JSON could be encoded by MessagePack like this:
```json
{
    "userName": "Martin",
    "favoriteNumber": 1337,
    "interests": ["daydreaming", "hacking"]
}
```
The first byte would indicate that what follows is an object with three entries (0x83). Then, each key would be encoded with:
- The type and the length of the key (example: String with 8 characters - userName: 0xae)
- The type and the length of the value (example: String with 6 characters - Martin: 0xa6)

This encoding would be 66 bytes long, which is less than the 81 bytes of the JSON representation.

### Thrift and Protocol Buffers

These are binary encoding libraries based on the idea of a schema and code generation. You define the schema of your
data in a language-independent format, and then you use a code generation tool to generate classes from that schema.

Examples:

**Thrift**
```
struct Person {
    1: required string       userName,
    2: optional i64          favoriteNumber,
    3: optional list<string> interests
}
```

The binary encoding for this schema would be similar to the MessagePack example. However, it doesn't need to include
the field names. Instead, it uses field tags (1, 2, 3) to identify them. Thrift also have a Compact Protocol, which
packs the tag number and the type into a single byte. It also uses variable-length integers to save space for small
numbers.

**Protocol Buffers**
```
message Person {
    required string userName       = 1;
    optional int64  favoriteNumber = 2;
    repeated string interests      = 3;
}
```

Protocol Buffers encode data similarly to Thrift's Compact Protocol, but it can save slightly a few more bytes.