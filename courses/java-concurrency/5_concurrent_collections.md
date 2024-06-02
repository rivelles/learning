# Advanced Java - Threads and Concurrency

## Concurrent Collections

In Java, Collections are not thread-safe by default. If we want to achieve thread-safety, there are a few options. For
example, if we want to access a List from multiple threads using an Iterator, it will throw a 
`ConcurrentModificationException` as a safeguard.

```java
public class SynchronizedCollectionsExamples {
    static List<Integer> myList = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        var thread1 = new Thread(SynchronizedCollectionsExamples::modifyList);
        var thread2 = new Thread(SynchronizedCollectionsExamples::modifyList);

        for (int i=0; i < 1_000; i++) {
            myList.add(i);
        }

        thread1.start();
        thread2.start();
        Thread.sleep(1000L);

        System.out.println("Finished. Size of myList: " + myList.size());
    }

    private static void modifyList() {
        for (Integer integer : myList) {
            myList.remove(integer);
        }
    }
}
```

Of course, we could use `synchronized` blocks to protect the access and prevent race conditions, but this is not the best
solution as it's prone to failures. Java provides us with:
- Synchronized Collections: A way of creating regular collections wrapped in synchronized blocks.
- Concurrent Collections: A way of creating collections that are optimized for concurrent access.

### Synchronized Collections

In synchronized collections, all methods are synchronized so they can perform thread-safe operations.

Examples:

```java
var synchronizedList = Collections.synchronizedList(new ArrayList<>());
var synchronizedMap = Collections.synchronizedMap(new HashMap<>());
var synchronizedSet = Collections.synchronizedSet(new HashSet<>());
```

The main drawback is that it can lead to performance issues, as only one thread can access the collection at a time, 
causing a lot of contention.  Java uses object-level locking to achieve this. 

Another drawback is that the iterators are not thread-safe.

### Concurrent Collections

Concurrent collections are optimized for concurrent access. The main ones are `ConcurrentHashMap`, `CopyOnWriteArrayList`
and `CopyOnWriteHashSet`.

`ConcurrentHashMap` is a thread-safe version of `HashMap`. It divides the whole map into smaller segments and only locks
the segment that is being accessed. This way, multiple threads can access the map at the same time. This technique is
called **lock striping**.

`CopyOnWriteArrayList` and `CopyOnWriteHashSet` are thread-safe versions of `ArrayList` and `HashSet`. They allow
reads to be done without locking, but writes are done by creating a new copy of the collection.