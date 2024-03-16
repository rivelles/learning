# Chapter 9: Consistency and Consensus

In the past chapter, we saw how distributed systems can fail. We need then to find ways to handle these failures
gracefully, since we cannot afford the entire system to go down every time there is a failure.

This chapter presents some algorithms that abstracts the complexity of handling failures from the application layer.
One of the most important concepts is **consensus**, which is the process of getting multiple nodes to agree on 
something.

For example, if you are using a database with single-leader replication and the leader goes down, the remaining nodes
can use consensus to elect a new leader.

## Consistency Guarantees

If you look at two different nodes of a database at the same time, it's likely that you will see different values, 
since data is being constantly updated and replicated.

Most replicated databases offer at lease an **eventual consistency** guarantee, which means that, if you stop writing 
data and wait for some time, all nodes will eventually return the same value. Another name for this is **convergent 
consistency**.

There is some similarities between consistency guarantees and transaction isolation levels, however, they mean different
things. Consistency guarantees handle data that is replicated across multiple nodes, whereas isolation level refers
to how database handles concurrent transactions.

The strongest consistency guarantee is **linearizability**.

### Linearizability

The idea behind linearizability is to behave as it there is only one copy of the data. As soon as the client makes a
write, the value is immediately visible to all clients, no matter which node they are reading from. This is also known 
as **atomic consistency**.

```mermaid
sequenceDiagram
    Client A->>+Database primary: UPDATE users SET name = 'Alice' WHERE id = 1
    Database primary->>Client A: ok
    Database primary->>+Replica 1: UPDATE users SET name = 'Alice' WHERE id = 1
    Replica 1->>Database primary: ok
    Client B->>+Replica 1: SELECT name FROM users WHERE id = 1
    Replica 1->>Client B: 'Alice'
    Database primary->>+Replica 2: UPDATE users SET name = 'Alice' WHERE id = 1
    Client B->>Replica 2: SELECT name FROM users WHERE id = 1
    Replica 2->>Client B: 'John'
    Replica 2->>Database primary: ok
```

This example shows a violation of linearizability. Client B gets 'John' as value on the second query, since the write
hasn't been replicated to the second replica yet.

For the next examples, let's abstract primaries and replicas and just call it a "Database". So when clients are querying
it doesn't matter which node they are querying from.

```mermaid
sequenceDiagram
    Client A->>+Database: SELECT name FROM users WHERE id = 1
    Database->>Client A: 'John'
    Client B->>Database: SELECT name FROM users WHERE id = 1
    Client C->>Database: UPDATE users SET name = 'Alice' WHERE id = 1
    Database->>Client B: 'John' or 'Alice'
    Client A->>Database: SELECT name FROM users WHERE id = 1
    Database->>Client A: 'John' or 'Alice'
    Database->>Client C: ok
    Client A->>Database: SELECT name FROM users WHERE id = 1
    Database->>Client A: 'Alice'
```

In this example, we have some constraints that we can assume:
- When client A first queries the database, client C didn't start the write operation, so the value returned is 
definitely 'John'.
- When client A and B queries the database for the second time, the write operation from client C has already started 
but not finished, so the value returned can be either 'John' or 'Alice'.
- When client A queries the database for the third time, the write operation from client C has already finished, so the
value returned is definitely 'Alice'.

This is still not enough to make this database linearizable, since when client A and B queries the database for the
second time, they can get different results. To make it linearizable, we need to add another constraint.

At some point in time, the value returned by the database must change from 'John' to 'Alice'. Once this happens, the
value returned by the database must be 'Alice' for all subsequent queries.

```mermaid
sequenceDiagram
    Client A->>+Database: SELECT name FROM users WHERE id = 1
    Database->>Client A: 'John'
    Client C->>Database: UPDATE users SET name = 'Alice' WHERE id = 1
    Client A->>Database: SELECT name FROM users WHERE id = 1
    Database->>Client A: 'Alice'
    Client B->>Database: SELECT name FROM users WHERE id = 1
    Database->>Client B: 'Alice'
    Database->>Client C: ok
    Client A->>Database: SELECT name FROM users WHERE id = 1
    Database->>Client A: 'Alice'
```

Now, when client A receives the value 'Alice', all other queries must return 'Alice' as well. This is the definition of
linearizability.

While linearizability and serializability are similar concepts, they are meant to solve different problems:
- Linearizability is about guaranteeing consistency between reads and writes of an object across different replicas.
- Serializability is about guaranteeing consistency between transactions writing to the same object.

A database can provide both serializability and linearizability, which is called **strict serializability**.

Serializability based on 2PL are typically linearizable, since when a write acquires a lock, other reades need to wait
until the write is finished.

Snapshot isolation, in the other hand, is not linearizable, since it allows reads to see the state of the database at
different points in time.

Some use cases for linearizability are:
- Implementations of locks and semaphores: if a client acquires a lock, it must be immediately visible to all other
clients.
- Unique constraints: primary keys, unique indexes, etc.

### Implementing Linearizability

When we talk about replicated databases, there are different types of them and each of them has its own way of making
writes visible to all clients.

- **Single-leader replication**: Pontentially linearizable, since you can make reads from the leader and replications
to be synchronous.
- **Multi-leader replication**: Not linearizable, since writes can be made to different leaders, they may produce
conflicts.
- **Leaderless replication**: Not linearizable. It seems that with a strict quorum system, it could be linearizable, but
we can have race conditions and conflicts due to multiple writes as well.

```mermaid
sequenceDiagram
    Writer->>+Replica 1: set x = 1
    Writer->>+Replica 2: set x = 1
    Writer->>+Replica 3: set x = 1
    Replica 1->>Writer: ok
    Reader A->>Replica 1: get x
    Replica 1->>Reader A: x = 1
    Reader A->>Replica 2: get x
    Replica 2->>Reader A: x = 0
    Reader A->>Replica 3: get x
    Replica 2->>Reader A: x = 0
```

In this example, even though we have data being replicated to all replicas synchronously, we can still get stale reads
after getting the updated value if we go to different replicas.

It **is** possible to make a leaderless replication system linearizable, at the cost of performance. The idea is to
perform read repairs synchronously and the writers to read the latest value from the quorum before writing the new
value. Some databases perform this, such as Cassandra, however they still can't guarantee linearizability because of
the way they handle conflicts (last-write-wins).

To make them linearizable, they would require a consensus algorithm, so it's safer to assume that **leaderless systems 
does not provide linearizability**.

### The Tradeoffs of Linearizability

Imagine we have a database distributed in two datacenters. It can be either a multi-leader or a single-leader system.
- If it is a multi-leader system and the network link goes down, both datacanters can continue operating, however,
we won't have linearizability, since data will eventually be stale.
- If it is a single-leader system and the network link goes down, the follower will stop receiving updates, if the 
application requires linearizability, it will stop operating for clients that are connected to the follower.

This issue can happen in any of the replication configurations, it doesn't even need to be in different datacenters.

So, if some replicas are disconnected:
- If your application requires linearizability, these replicas cannot serve any operation.
- If not, each replica can process requests independently, but you can get stale reads, making the application more
**available**.

#### CAP Theorem

The CAP theorem is usually stated as Consistency, Availability and Partition Tolerance, and that we need to pick two,
but this is not quite complete.

Partition tolerance is something that will always happen, we can't avoid it. A better way of rephrasing it is:
- When a network **partition** happens, we need to choose between **consistency** and **availability**.

CAP usually only considers consistency model (linearizability) and just one kind of fault (network partition). It 
doesn't consider other kinds of faults, such as node failures, which can also affect the system, so we need to consider 
other tradeoffs as well instead of relying purely on it.

#### Few Systems are Actually Linearizable

If we think on a single computer operating, we'd expect that if we write a value to its memory, all subsequent reads
will return the same value, making it linearizable.

However, if we have multiple threads running in multiple cores, each of them with its own cache, we can get stale reads
as well, since the caches are not synchronized.

Could this be different? Yes, but it would be much slower, so the decision here is not between network partition and
linearizability, but between performance and linearizability. The chosen was to make it faster, and it is like that
for many other systems.

### Ordering Guarantees

Ordering concept is important in distributed systems because it helps to preserve causality.
- On an application that has a question being answered by another, there must be a causal dependency between them.
- When there is an update operation, there was an insert one before it.
- If we have two operations A and B, either A happened before B, B happened before A, or they are concurrent. If they 
are concurrent, there is no causal dependency between them.

Causality imposes ordering of events. One thing leads to another.

A linearizable system behaves as if the data is stored in a single place and every operation is atomic. This implies
that we have a **total order** of operations, because we are able to tell which happened before the other.

Two events are ordered it there is causality between them. If they are concurrent, they are incomparable. So, if a system
is not linearizable, it provides a **partial order** of operations.

Therefore, in a linearizable database, there must be a single timeline which all operations are totally ordered, without
any concurrency.

It's safe to say that linearizability implies causality. Any system that is linearizable will preserve causality
of its operations.

We saw that many systems abandoned linearizability because of performance penalties. A middle ground is possible because
linearizability is not the only way to support causality. New databases are being researched to provide solutions that 
preserve causality with performance and availability characteristics that are similar to eventual consistent ones. This
is a promising direction but we don't have many systems in production yet.

#### Capturing the Causal Order

In order to maintain causality, we need to know which operation happened before which other operation.

Concurrent operations might happen, but they must be processed in the same order in all replicas. In order to do this, 
we need to find a way of describing the "knowledge" of each node in the system about values they are storing.

#### Sequence Number Ordering

Keep track of all causalities can become impractical due to the amount of data systems process nowadays.

One way systems do to track causality is to assign a sequence number to its operations. It can be generated as a
timestamp (not using the system's clock due to its unreliability), but a logical clock. This is consistent with 
causality: if A happened before B, then A's sequence number is less than B's.

This is useful in databases with single-leader replication, where the leader can define the sequence number for each 
operation and, when followers read the replication log, they can know which operations happened before others.

If we are using multi-leader replication, there are several ways to assign sequential numbers:
- Each node can generate sequence numbers with reserved allocated ranges, so they don't overlap.
- Each node can attach a timestamp to the operation
- If we have only two nodes, one can assign even numbers and the other odd numbers.

However, neither of these approaches are consistent with causality. We can't tell which operation happened before the
other.

#### Lamport Timestamps

The only method that is consistent with causality in this type of replication is called **Lamport timestamps**.

Each node has a unique identifier and a counter of number of operations it has processed. This counter will always be
the maximum number of operations in the node.

```mermaid
sequenceDiagram
    Client A->>+Node 1: write x (max = 0)
    Node 1->>Client A: ok (1, 1)
    Client B->>+Node 2: write x (max = 0)
    Node 2->>Client B: ok (1, 2)
    Client B->>Node 2: write x (max = 1)
    Node 2->>Client B: ok (2, 2)
    Client B->>Node 2: write x (max = 2)
    Node 2->>Client B: ok (3, 2)
    Client A->>Node 2: write x (max = 1)
    Node 2->>Client A: ok (4, 2)
    Client A->>Node 1: write x (max = 4)
    Node 1->>Client A: ok (5, 1)
```

Notice that, for every request and response, there is a max representing the counter of operations. When client write
to the node, it sends the max it has seen so far. If the node has a max bigger than this number, it will return its max
plus one. Notice how this happens in the past example when client A sends its max as 1 and receives 4 as response.

The causality here is maintained by using the max number of operations, and if they are the same, the node's unique
identifier is used to break the tie.