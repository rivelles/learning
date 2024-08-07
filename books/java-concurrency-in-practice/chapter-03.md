# Sharing Objects

This chapter analyses how to safely share objects across different threads, which is fundamental to build complex thread
safe applications.

Synchronization is not only about executing a piece of code in an isolated way. It's also about **memory visibility**.
We don't want only to ensure that only a thread can modify something at a time, we also want to ensure that other threads
can also see the changes that were made.

## Visibility

In a single-threaded environment, when we write a value to a variable, it's guaranteed that in the next read we are 
going to get the most updated value as well. However, when reads and writes occur in different threads, this might not
be the case.

The following program illustrates the issue. The main thread might never print the number, or even might print 0, since
there is no guarantee on the order of operations. This is because the JVM allows reordering of operations to take
performance advantages over multiprocessor hardware.

```java
public class NoVisibility { private static boolean ready; private static int number;
    private static class ReaderThread extends Thread { public void run() {
    while (!ready)
        Thread.yield();
    System.out.println(number);
    }
}
public static void main(String[] args) {
        new ReaderThread().start();
        number = 42;
        ready = true;
    }
}
```
The easiest way to avoid those issues is to **always use proper synchronization whenever data is shared across threads**.

This is another simple example about stale data. A thread calling `get()` may not see writes made by another thread 
calling set(). Synchronizing only `set()` is not sufficient.

```java
@NotThreadSafe
public class MutableInteger {
    private int value;
    
    public int get() { return value; }
    public void set(int value) { this.value = value; } 
}
```

Intrinsic locking guarantees that, once a thread finishes executing a synchronized block, all changes that were made are
visible to other threads when they acquire the same lock.

To ensure that all threads can see the most up-to-date values, the reading and writing threads must synchronize on the 
same lock.

### An issue with long and double types

When a thread reads stale value, it means that it might not be the most up-to-date value, but at least it's a value that
was written once by some thread. One exception to this are the long and double types.

They are 64-bit numbers and, internally, the JVM kit threats reads and writes to them as two different operations
to 32-bit numbers. So, it's possible that a stale value gets the new value for the first 32 bits and the old value for 
the last 32- bits.

### Volatile variables

A weaker form of synchronization. When a variable is declared synchronized, Java guarantees that all changes made to it
will be propagated to other threads safely. These variables won't be cached in the registers nor other caches, so
whenever a thread reads them, they are always getting the most up-to-date value.

Accessing a volatile variable doesn't require acquiring locks so threads won't be blocked if they need to write to it,
making it a light-weighted version of synchronization.

The most common use cases for that are status flags to indicate signals such as completion or interruption of some
processing, for example in this example, where we have one anthropomorphized thread counting sheeps only while it's not 
sleeping:

```java
volatile boolean asleep;
...
while (!asleep) 
    countSomeSheep();
```

**Locking can guarantee both visibility and atomicity; volatile variables can only guarantee visibility.**