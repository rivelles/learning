# Thread Safety

There are numerous definitions of thread safety. At the heart of the definition, we can say that thread safety relates
to **correctness**.

This means that a class conforms to its **specification**, which is guarded by **invariants** constraining objects'
state and conditions. A class that has no state is thread safe by default, for example:

```java
@ThreadSafe
public class StatelessFactorizer implements Servlet {
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        encodeIntoResponse(resp, factors);
    }
}
```

This class has no fields neither references fields from other classes. The state for a single computation is only visible
to the thread that is running it, so it can't be influenced or influence another thread.

Servlets by itself are thread safe by default if we don't want to keep state on them. If, for example, we want to 
remember things from one request to another, like the number of requests it received, then we need to ensure thread 
safety.

## Atomicity

Let's add a state to the previous servlet:

```java
@NotThreadSafe
public class UnsafeCountingFactorizer implements Servlet {
    private long count = 0;
    public long getCount() { return count; }
    
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        ++count;
        encodeIntoResponse(resp, factors);
    }
}
```

While the increment action might seem safe, this operation doesn't happen as a single indivisible operation, it's a 
classic _read-modify-write_ flow. Therefore, we say it's not **atomic**. If two threads try to increment it simultaneously,
with some unlucky we can have both reading the same value (let's say, 9), and adding one to it, which is clearly not what
is supposed to happen, since an increment was lost.

The presence of the possibility of having inaccurate results because of unlucky timing is a phenomenon called race 
condition.

### Race condition

A race condition happens when getting the correct result of an operation depends on correct timing. The most common
type of race condition is the so called check-then-act, when a potentially stale check might lead to an inaccurate act.

### Compound actions

In order to make the sequence of operations we previously saw atomic, we need a way to prevent other threads to mutate
the state of a variable while another thread operating on it.

To ensure thread safety, _check-then-act_ operations must be atomic. We refer to those types of operations as **compound
actions**. One way to fix it is by using the `java.util.concurrent.atomic` atomic classes. In our example we could simply
rely on the AtomicLong to ensure the atomicity of the increment.

```java
@ThreadSafe
public class CountingFactorizer implements Servlet {
    private final AtomicLong count = new AtomicLong(0);
    
    public long getCount() { return count.get(); }
    
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        count.incrementAndGet();
        encodeIntoResponse(resp, factors);
    }
}
```

## Locking

We were able to ensure thread safety in our servlet, but what if we want to have more variables defining it state?

For example, if we want to cache the most recently computed result, as being the last number received and its factors.

```java
@NotThreadSafe
public class UnsafeCachingFactorizer implements Servlet {
    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<BigInteger>();
    private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<BigInteger[]>();
    
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber.get()))
            encodeIntoResponse(resp, lastFactors.get());
        else {
            BigInteger[] factors = factor(i);
            lastNumber.set(i);
            lastFactors.set(factors);
            encodeIntoResponse(resp, factors);
        }
    }
}
```

This approach doesn't work. Even though the individual operations on `lastNumber` and `lastFactors` are thread safe, the
class has race conditions that might violate the invariants of its state. One of them is that what we store in 
`lastFactors` are always the factors for the `lastNumber` value.

When multiple variables participate in an invariant, we need to update all of them in the same atomic operation.

### Intrinsic lock

Java provides a built-in lock mechanism, invoked by using the keyword `synchronized`. It has two parts:
- The reference to the object which will be served as the lock.
- The block of code guarded by that lock.

We can also synchronize an entire method, which will be basically a lock guarded by the object that owns the method (`this`)
and the block of code will be the entire method.

```java
synchronized(lock) {
    // Access or modify shared state guarded by lock
}
```

Every Java object can act as a lock. It's automatically acquired by the thread when entering the block and released when
exiting it, either normally or when it throws an exception.

It acts as mutexes. When thread A attempts to acquire a lock held by thread B, thread A must wait until B releases it.
If B never releases, A waits forever.

We can now make our previous example thread safe:

```java
@ThreadSafe
public class SynchronizedFactorizer implements Servlet {
    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;
    
    public synchronized void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber))
            encodeIntoResponse(resp, lastFactors);
        else {
            BigInteger[] factors = factor(i);
            lastNumber = i;
            lastFactors = factors;
            encodeIntoResponse(resp, factors);
        }
    }
}
```

Although this is thread safe, this is not a good approach from the performance point of view, since it inhibits multiple
clients calling the servlet simultaneously.

#### Reentrancy

If a thread tries to acquire a lock it already has, it will succeed. Reentrancy means that locks are acquired by threads,
not by invocations.

Each lock has a count, when it's zero it means that it is not being hold by any thread. When a thread acquires that lock,
it's count is incremented, and when they exit the synchronized block, the count is decremented and the lock is released.

The following code could deadlock if reentrancy was not used, because LoggingWidget overrides the synchronized method
provided by Widget and the call to `super.doSomething()` would never be able to acquire the lock.

```java
public class Widget {
    public synchronized void doSomething() {
        // ...
    }
}

public class LoggingWidget extends Widget {
    public synchronized void doSomething() {
        System.out.println(toString() + ": calling doSomething");
        super.doSomething();
    }
}
```

## Guarding State with Locks

When we use synchronization to coordinate access to a variable, it is needed in **every** piece of code that variable is
accessed. Also, the **same lock** needs to be used to access it.

In the servlet example we saw, `lastNumber` and `lastFactors` are guarded by the servlet's intrinsic lock. This is a 
valid approach, since it won't prevent other threads to use that object, it will only prevent other threads to acquire
that lock.

A common convention is to encapsulate all mutable state within an object and synchronize code paths that access mutable
data with its intrinsic lock.

"For every invariant that involves more than one variable, all the variables involved in that invariant must be guarded 
by the same lock."

## Liveness and Performance

The example about using the intrinsic lock in the servlet guarantees the atomicity on the operations handling`lastNumber` 
and `lastFactors`, however, we introduced an important performance penalty.

We have subverted the main principle of using the servlets framework (multiple threads executing requests at the same
time), since now each thread needs to wait for a lock.

We can restructure this example and synchronize only the pieces of code that actually need it.

```java
@ThreadSafe
public class CachedFactorizer implements Servlet {
    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;
    @GuardedBy("this") private long hits;
    @GuardedBy("this") private long cacheHits;
    
    public synchronized long getHits() { return hits; }
    
    public synchronized double getCacheHitRatio() {
        return (double) cacheHits / (double) hits;
    }
    
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = null;
        synchronized (this) {
            ++hits;
            if (i.equals(lastNumber)) {
                ++cacheHits;
                factors = lastFactors.clone();
            }
        }
        if (factors == null) {
            factors = factor(i);
            synchronized (this) {
                lastNumber = i;
                lastFactors = factors.clone();
            }
        }
        encodeIntoResponse(resp, factors);
    }
}
```

This example holds the lock when it needs to access the state variable, and only for the duration of the compound actions.
With this, we are able to balance between performance and consistency. It's important to reason about them to avoid narrowing
too much or too few the scope of synchronized blocks.