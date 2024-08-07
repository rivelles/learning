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

## Publication and Escape

Publishing means making an object available to outside its current scope, which includes:
- Storing a reference to where other code can find it;
- Returning it in a non-private method;
- Passing it to a method as an argument in another class.

Publishing objects may compromise encapsulation and makes it harder to guarantee its invariants. An object that is
published when it shouldn't is being said to have _escaped_.

The following example is the simplest form of escaping: declaring an object as public static:
```java
public static Set<Secret> knownSecrets;

public void initialize() {
    knownSecrets = new HashSet<Secret>();
}
```

Any piece of code can read and modify the set of secrets.

This other example shows how escaping can happen by returning a reference to an object from a method:
```java
class UnsafeStates {
    private String[] states = new String[] {
            "AK", "AL" ...
    };
    public String[] getStates() { return states; } 
}
```

Now, any caller can modify the `states` content, this is not a desirable intent.

Another example that can be problematic is by escaping the reference of this. This happens when escaping inner class 
instances:
```java
public class ThisEscape {
    public ThisEscape(EventSource source) {
        source.registerListener (new EventListener() {
            public void onEvent(Event e) { 
                doSomething(e);
            }
        });
    }
}
```

A common mistake that leads to this case is when a thread is started from the constructor of a class. The new thread 
might be able to see the object before it's fully constructed.

To avoid this scenario, we can use a private constructor and expose a public factory method:

```java
public class SafeListener {
    private final EventListener listener;
    private SafeListener() {
        listener = new EventListener() {
            public void onEvent(Event e) { 
                doSomething(e);
            }
        };
}
    public static SafeListener newInstance(EventSource source) { 
        SafeListener safe = new SafeListener(); 
        source.registerListener(safe.listener);
        return safe;
    }
}
```

Another example of letting this escape is shown in the following code:

```java
public class Foo {
    public Foo() {
        doSomething();
    }

    public void doSomething() {
        System.out.println("do something acceptable");
    }
}

public class Bar extends Foo {
    public void doSomething() {
        System.out.println("yolo");
        Zoom zoom = new Zoom(this); // at this point 'this' might not be fully initialized
    }
}
```

## Thread Confinement

We already saw that, to ensure thread safety, we need mutable data to be shared using synchronization. Another way to
achieve that is by simply **not share**. This technique is called thread confinement. When an object is confined to a 
thread, it is automatically thread-safe.

A common use case of thread confinement is pooled JDBC `Connection` objects. When a thread acquires a connection, it
uses it for processing a request and returns it. This pattern implicitly confines the `Connection` to that thread for
the duration of the processing.

Following is an example of stack confinement, where we have a SortedSet instantiated and referenced by the `animals` 
variable. This reference is only accessed within the `loadTheArk` method, therefore it is confined to the current thread.
If we were to return the SortedSet, the confinement would be violated and the _animals would escape_.

```java
public int loadTheArk(Collection<Animal> candidates) { SortedSet<Animal> animals;
    int numPairs = 0;
    Animal candidate = null;
    animals = new TreeSet<Animal>(new SpeciesGenderComparator()); 
    animals.addAll(candidates);
    
    for (Animal a : animals) {
        if (candidate == null || !candidate.isPotentialMate(a)) 
            candidate = a;
        else {
            ark.load(new AnimalPair(candidate, a)); ++numPairs;
            candidate = null;
        } 
    }
    return numPairs;
}
```

### ThreadLocal

A more formal way to ensure thread confinement. When using ThreadLocal, get and set methods will be provided and they
will maintain a separate copy of the object for each thread that calls it. So, `get()` will return the most recent value
defined by the `set()` **from the current thread**.

```java
private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>() {
    public Connection initialValue() {
        return DriverManager.getConnection(DB_URL);
    }
};

public static Connection getConnection() { 
    return connectionHolder.get();
}
```

This example holds a global shared Connection, but each thread that gets it will get a copy of the initial value.

Conceptually, `ThreadLocal<T>` can be seen as a `Map<Thread, T>`. (This is not how it's actually implemented)