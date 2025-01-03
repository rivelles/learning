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

Notice that, for every request and response, there is a max representing the counter of operations. When clients write
to the node, it sends the max it has seen so far. If the node has a max bigger than this number, it will return its max
plus one. Notice how this happens in the past example when client A sends its max as 1 and receives 4 as response.

The causality here is maintained by using the max number of operations, and if they are the same, the node's unique
identifier is used to break the tie.

Lamport timestamps are similar to version vectors, the difference is that version vectors can distinguish between
concurrent operations and causally related ones; while Lamport timestamps can only enforce total ordering, you can't
tell if two operations are concurrent or not.

#### Timestamp ordering is not enough

Although Lamport timestamps are useful to maintain causality, they are not enough to solve some common problems in 
distributed databases.

Imagine we have a unique constraint that a user need to have a unique username. If two users try to create an account
concurrently, the system would need to detect this and reject one of them.

At first, it seems easy to solve this problem. We just need to pick the operation with the lowest timestamp and reject
the other one. However, this works only for determining the winner **after** the fact has already happened. If the 
system needs to decide right now if the request should succeed or fail, this approach doesn't work, since at that
moment, the node doesn't know that other node is processing the same information.

In order to achieve this constraint, we would need to check with all other nodes which timestamps they have seen so far,
and if other node can't answer our system would grind to a halt, which is not desirable in a distributed environment.
For example, in this example, if we insert a username, the node needs to check with all other nodes if they have created
the same username, and if they have, it needs to reject the operation.

### Total Order Broadcast

In single-leader replication, the total order is guaranteed by using a single CPU core in the leader to process all
sequences of operations. The main challenges are:
- What happens when the leader fails?
- How to scale the system for larger throughput?

Total order broadcast is a protocol for exchanging messages between nodes with two properties:
- No messages are lost: messages need to be delivered to all nodes.
- All nodes deliver the messages in the same order.

These constraints need to be satisfied even if the nodes are failing or there is a network partition.

This is implemented in consensus services such as ZooKeeper and etcd.

Another way of looking at this is using a log, such as a replication log, a write-ahead log or a transaction log.
Delivering a message is like appending an entry to the log. Since all nodes need to deliver the same messages in the 
same order, all of them can read the log and see the same messages in the same order.

This is also useful for implementing a lock service that uses **fencing tokens**.

#### Linearizability and Total Order Broadcast

If you have total order broadcast, you can implement a linearizable storage on top of it. However, they are not the same
concept.
- Total order broadcast is asynchronous, it doesn't guarantee that the message will be delivered immediately. So we can
have nodes lagging behind others.
- Linearizability talks about read operations, making sure we won't see stale values.

However, by having a total order broadcast, a linearizable storage can be built around it.

Example: Having a unique constraint on a username.
By using a compare-and-set operation, we can make sure the username will be set only if it doesn't yet exist. If we have
a register for the username, it will be set only if its previous value is null. The logic could be:
1. Append a message in the log with the operation to attempt to set the username.
2. Read the log entries until we find the message we just appended.
3. If the first message for the desired username is the one we appended, append another message with the confirmation 
and acknowledge it to the client.

If there are several concurrent writes, all nodes can agree what is the winner, since they will all read the logs in the
same order.

This approach guarantees linearizable writes, but it doesn't avoid stale reads, because we can read from nodes that are
behind the leader.

We can think the other way around as well. If we have a linearizable storage, we can implement total order broadcast on
top of it.

Assuming we have a linearizable register that stores an integer, we can use a compare-and-set operation to increment the
value and attach it to all messages we send to the nodes. With this, nodes can control the order of messages they 
process.

### Distributed Transactions and Consensus

After discussing transactions, replication, linearizability and total order broadcast, we can now talk about consensus,
which is a very important and discussed topic. This basically tells that nodes need to agree on something, which can
be:
- Leader election: In a database with single-leader replication, all nodes need to agree on who is the leader. Consensus
becomes important in case of failover. A bad election could result in a split-brain scenario.
- Atomic commit: In a distributed transaction, all nodes need to agree on the outcome of a transaction that can
possibly fail.

#### Atomic Commit and Two-Phase Commit (2PC)

On a single node, when a transaction runs, first it modifies the data, then it changes the commit record. If a database
crashes during this process, when it comes back, it can either rollback the changes or commit them, depending on the
commit record values.

However, it becomes more complicated in a distributed environment. If some nodes commit a transaction and some don't, we
can't rollback the already committed ones, making them inconsistent across nodes. For this reason, a node can only 
commit if all other nodes are also going to commit.

Remember: If a node committed, this data becomes visible to clients, so we can't rollback it. This is the basic idea
behind **read committed isolation level**.

> We can use compensating transactions to rollback a transaction that has already been committed. This is a common
pattern but refers more to business logic than to database transactions.

In the two-phased commit algorithm, the commit-abort process is divided into two phases.

```mermaid
sequenceDiagram
    Coordinator->>DB 1: write data
    DB 1->>Coordinator: ok
    Coordinator->>DB 2: write data
    DB 2->>Coordinator: ok
    Coordinator->>DB 1: prepare
    Coordinator->>DB 2: prepare
    DB 1->>Coordinator: ok
    DB 2->>Coordinator: ok
    Coordinator->>DB 1: commit
    Coordinator->>DB 2: commit
    DB 1->>Coordinator: ok
    DB 2->>Coordinator: ok
```

2PC uses a new component, called a coordinator, or a transaction manager. It's often implemented as a library in the
application code.

The algorithm starts with the application reading and writing data normally to the database nodes. When the application
wants to commit, it sends a prepare message to all nodes.
- If all participants say yes, indicating they are ready to commit, the coordinator sends a commit message to all of
them.
- If any participant says no, indicating they are not ready to commit, the coordinator sends an abort message to all of
them.

In more details, it will:
1. When the application begins a distributed transaction, it requests a transaction ID to the coordinator.
2. The application begins writing to the nodes attaching the transaction ID, if anythingoes wrong, it can rollback.
3. When the application is ready to commit, the coordinator sends a prepare message to all nodes. If any of these calls
fail for any reason, the coordinator sends an abort message to all nodes.
4. When a node receives a prepare request, it makes sure that the data is consistent and will be able to commit. By
replying yes, it says that it's ready to commit and surrenders the right to abort.
5. When the coordinator receives the responses for all nodes, it takes a decision of committing or aborting, depending
on the responses. It writes the decision to disk so it knows what to do in case of a crash.
6. The commit or abort is sent to all participants. If this request fails, the coordinator will retry until it succeeds.
Even if the node crashed, it will be forced to commit when it recovers since it voted "yes" in the prepare phase.

This protocol has two points of no-return: when the node votes "yes" it basically says it will be able to commit no
matter what, and when the coordinator decides to commit, that decision is irrevocable.

Similarly to a marriage ceremony, once the participants already said "I do", there is no turning back. Even if one of
them faints, when they wake up, they will be married.

#### Coordinator failure

If the coordinator crashes before sending the commit after a node has voted "yes", this transaction will be stuck in
the "prepared" state waiting for the commit to come from the coordinator. A timeout wouldn't help here because it might
make the node inconsistent with another one that committed.

The only way to recover is to wait for the coordinator to be running again. When it comes back, it will read the log
and send the commit or abort messages to the nodes.

### Distributed Transactions in Practice

Many cloud services chose not to implement distributed transactions using 2Pc due to operational problems, such as
what happens when a coordinator crashes and low performance. It carries a heavy performance penalty (distributed 
transactions in MySQL are 10x slower than local transactions, for example). This is because of the additional disk
forcing (fsync - "flush the data") required for crash recovery and additional network roundtrips.

Distributed transactions can be homogeneous or heterogeneous.
- In n homogeneous model, all nodes involved are using the same software with the same version, therefore, they can
understand the same protocol.
- In a heterogeneous model, nodes are using different software, and they all need to agree on the same protocol, 
otherwise it's impossible to have a distributed transaction.

For example, imagine we want to acknowledge a message in a queue only after we have written the data to the database.
It's possible to implement it if both the database and the queue are using the same protocol.

If one side effect of processing the message is to send an e-mail and th e-mail server doesn't support 2PC, it could
happen that the e-mail is sent more than once.

#### XA Transactions

XA is a standard for implementing 2PC in a distributed heterogeneous environment. It's supported by many databases and
message brokers, such as PostgreSQL, MySQL, DB2, SQL Server, Oracle, ActiveMQ, IBM MQ.

The transaction coordinator is usually implemented as a library in the application code, which implements the XA API.
It will keep track of the participants in a transaction, collect their responses from the prepare phase and use a log
on disk to keep track on the commit/abort decisions.

If the application crashes, any participants with prepared transactions will get stuck. The application server must
be restarted and the coordinator library needs to read the log to process the transactions that are stuck. Then it will
send the commit or abort messages to the participants.

##### Locks

The main problem on this kind of transactions is that, when a node is stuck in the prepared state, it might have locks
in several rows in the database. If it's using a read committed isolation level, rows that will be written will be
locked; if it's using repeatable read/snapshot isolation, it will lock rows that will be read too. Therefore, if the
coordinator crashes before committing/aborting, these locks will be held indefinitely.

If the coordinator can't decide what to do - maybe because the log file was lost or corrupted, these transactions need
to be resolved manually by an administrator. They need to evaluate if the transaction can be committed or aborted and
then send the message to the coordinator to do so.


#### Limitations of XA Transactions

- The coordinator becomes a single point of failure.
- If we have stateless server-side applications, making them handle transactions transforms them into stateful.

### Fault-Tolerant Consensus

Consensus means making different nodes agree on something. The problem is usually normalized as: different nodes are 
proposing values, and the algorithm needs to decide on one of those values.

When we saw 2PC, the coordinator was the component making all the decisions, however, if it goes down, the system might
become stuck, therefore, this is not a **fault-tolerant** consensus algorithm. The most known fault-tolerant consensus
algorithms are **Paxos** and **Raft**, **VSR** and **Zab**.

Although they are different, they share some similarities. They decide on a sequence of values, making them **total 
order broadcast** algorithms.

Is single-leader replication a consensus algorithm?
In this setup, we can have  two types of leader election:
1. The leader is elected manually, making all writes go through it. If this node goes down, the whole system is 
unavailable to accept writes, making it not fault-tolerant.
2. The leader is elected automatically, with this, the system can decide on a new leader if the current one goes down.

However, we can still have problems. We saw previously that we can fall into a split-brain scenario, where two nodes
believe they are both leaders. To solve this, we need another consensus, but the algorithm we saw requires a leader, so
we fall into a conundrum scenario.

#### Epoch numbering

The algorithms we saw are designer to work with a leader, however, they don't need to guarantee that the leader is unique.
Instead, they can use an epoch number and guarantee that, within an epoch, there is one unique leader.

If the leader goes down, the nodes will elect a new one and increase the epoch number. If there is a conflict because
the previous leader is still alive, the leader with the highest epoch number will win. A node can't only trust its own
judgment, it needs to know if other nodes also consider it as the leader. It does that by collecting votes from a quorum
of nodes. If a leader wants to make a decision, it needs to collect votes from this quorum. A node will vote in favour
only if it believes the leader has the highest epoch number.

This is very similar to 2PC, but the main differences are:
- The coordinator is elected by the nodes.
- Not **all** nodes need to agree on the decision, only a quorum.