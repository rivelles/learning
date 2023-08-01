# Chapter 3: Storage and Retrieval

This chapter will basically explain how data can be stored and retrieved in different types of databases.

Imagine the simplest example: a text file, which can be structured as lines of key/value pairs separated by commas.

```
12345,The number of the beast
42,The answer to life and everything
```

If we are to insert new values, it would have a pretty good performance. All we need to do is **append** a new line to the file. 

However, if we want to retrieve a value, we would need to read the whole file and search for the key. This is not very efficient,
having a cost of O(n).

If we want to make the retrieve operation efficient, we need another separate structure: an _index_. The idea is to keep additional
metadata derived from the file, which will help us find the value we are looking for. This structure is updated every time we insert
a new value in our file.

The most important tradeoff here is that: **adding an index makes writes slower, but makes reads faster**.

## Hash indexes

In the same example, let's say our index is a hash table, which maps from keys to byte offsets in the file. The hash table is kept in
memory, so it is small enough to be read quickly.

When we want to retrieve a value, we first look up the key in the hash table, which gives us the byte offset in the file. Then we can seek
to that position in the file and read the value.
```
12345,The number of the beast\n42,The answer to life and everything
```

| Key   | Byte offset |
|-------|-------------|
| 12345 | 0           |
| 42    | 31          |

### Segmentation and compaction

The problem with this approach is that the file will grow over time, and the hash table will no longer fit in memory. One solution is to
split the file into segments of a certain size, and have a separate hash table for each segment. This is called **segmentation**.

When we are writing to our file, we append the new key/value pair to the end of the file. If the segment is full, we can throw away
the old values of each key, keep only the most recent value and write them into a new segment. This is called **compaction**.

Imagine a file that appends key-values of views per cat videos. We have a segment that was compacted into a new segment.

|           |           |           |          |           |          |
|-----------|-----------|-----------|----------|-----------|----------|
| mew:1078  | purr:2103 | purr:2104 | mew:1079 | mew:1080  | mew:1081 |
| purr:2105 | purr:2106 | purr:2107 | yawn:511 | purr:2108 | mew:1082 |

↓↓ Compaction process ↓↓

|          |           |          |
|----------|-----------|----------|
| mew:1082 | purr:2108 | yawn:511 |

Each segment has its own hash table, so if we look for a key, we will first try to find it in the most recent segment's hash table. If we
don't find it, we will look in the previous segment's hash table, and so on.