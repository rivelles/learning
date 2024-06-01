# Advanced Java - Threads and Concurrency

## Synchronization

### Memory access and problems

Threads are independent execution sequences that make use of shared memory. The RAM memory is categorized into stack and
heap. In a nutshell:
- The stack memory is used to store primitive variables and references to objects.
- The heap memory is used to store objects.

When a thread is created, it will have its own **stack memory**, and it will be private to that thread. However, the
**heap memory** is shared among all threads.

```mermaid
graph TD
    classDef Heap fill:#BDFFA4,stroke-width:0,color:grey,font-size:20px;
    classDef Stack fill:#A2FAF3,stroke-width:0,color:black,font-size:10px;
    classDef Thread fill:#A2FAF3,stroke-width:0,color:black,font-size:10px;
    classDef Memory stroke-width:0,color:black,font-size:10px;
    
    A[Thread 1] --- B[Stack Memory 1]
    D[Thread 2] --- E[Stack Memory 2]
    A --- C[Heap]
    D --- C
    subgraph SG1["RAM"]
        B
        C
        E
    end
    
    class C Heap
    class A,D Thread
    class B,E Stack
    class SG1 Memory
```

So, the stack for each thread will hold local variables and references to objects that are in the heap.

If we have a shared object between two threads, for example, a counter, there is no guarantee that the threads will see
the same value of the counter and, when incrementing it, the value might be lost.

If we see the example at [this class](courses/java-concurrency/concurrency-project-examples/src/main/java/org/concurrency/example/chapter_1/ConcurrentAccessRunnable.java) 
there is no deterministic output, because both threads are sharing the same Runnable instance and the counter is being
incremented by both threads, so one can get stale data and some increments might be lost.

Let's add more information to the diagram by representing the CPU cache and registers:

```mermaid
graph TD
    classDef Heap fill:#BDFFA4,stroke-width:0,color:grey,font-size:20px;
    classDef Stack fill:#A2FAF3,stroke-width:0,color:black,font-size:10px;
    classDef Thread fill:#A2FAF3,stroke-width:0,color:black,font-size:10px;
    classDef Memory stroke-width:0,color:black,font-size:10px;
    classDef CPU stroke-width:0,color:black,font-size:10px;
    classDef CPUCache fill:#FFD700,stroke-width:0,color:black,font-size:10px;
    classDef CPURegisters fill:#FFD700,stroke-width:0,color:black,font-size:10px;
    
    subgraph SG2["CPU 1"]
        A[Thread 1] --- F[Register]
        F --- H[Cache Memory]
    end
    subgraph SG3["CPU 2"]
        D[Thread 2] --- G[Register]
        G --- I[Cache Memory]
    end
    SG2 --- B[Stack Memory 1]
    SG3 --- E[Stack Memory 2]
    C[Heap]
    subgraph SG1["RAM"]
        B
        C
        E
    end
    
    class C Heap
    class A,D Thread
    class B,E Stack
    class SG1 Memory
    class SG2,SG3 CPU
    class F,G CPURegisters
    class H,I CPUCache
```

The CPU has its own register and cache memory, which have a much faster access time than the RAM memory. When the past
example was running, a possible issue is that this sequence of execution happens:
1. Thread 1 reads the counter value from the heap and stores it in the cache memory (counter = 0).
2. Thread 2 reads the counter value from the heap and stores it in the cache memory (counter = 0).
3. Thread 1 increments the counter in the cache memory (counter = 1).
4. Thread 2 increments the counter in the cache memory (counter = 1) and to the heap (counter = 1).
5. Thread 1 writes the counter value it has in the cache memory to the heap (counter = 1).

The expected value here would be 2, because we had 2 increments. However, the value is 1 because the increment of Thread
2 was lost.