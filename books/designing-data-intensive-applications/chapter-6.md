# Chapter 6 - Partitioning

Partitioning means splitting a database into multiple parts called partitions. Each partition is located on a different
machine. Queries and writes can be routed to the correct partition based on the value of a key, which makes scalability
easier.

Partitioning is usually combined with replication, so that copies of each partition are stored on several machines.

## Partitioning of Key-Value Data

Our goal is to split the data evenly across partitions, so that all partitions have roughly the same size. If this 
doesn't happen, we call it **skewed**. A partition with a lot more data than the others is called a **hot spot**.

One way to partition is to assign range of keys to each partition. For example, for an encyclopedia, we could assign
ranges of letters to each partition. This is called **range partitioning**.

```mermaid
flowchart 
    A[Partition 1: A - F]
    B[Partition 2: G - L]
    C[Partition 3: M - R]
    D[Partition 4: S - Z]
```

The downside of this approach is that it can lead to hot spots. In our example, if we have a lot of articles starting
with the letter A, the partition that stores the A range will be a hot spot.

Due to this risk, range partitioning is not used very often. Instead, we can use **hash partitioning**. We take a
hash of the key and use the result to determine which partition the key belongs to.

We then distribute the ranges of hashes evenly across partitions. For example, if we have a hash function that returns
an integer between 0 and 99, we can assign the following ranges to each partition:

```mermaid
flowchart 
    A[Partition 1: 0 - 24]
    B[Partition 2: 25 - 49]
    C[Partition 3: 50 - 74]
    D[Partition 4: 75 - 99]
```

By applying the hash function to the key, which could be a timestamp, we can determine which partition the key belongs,
such as:
- 2024-01-01 00:00:00 -> 25 -> Partition 2
- 2024-01-01 00:00:01 -> 62 -> Partition 3
- 2024-01-01 00:00:02 -> 2 -> Partition 1
- 2024-01-01 00:00:03 -> 99 -> Partition 4

Although it avoids hot spots, it has a downside: it's not possible to efficiently query a range of keys. Also, if we
always insert the same key, it will always go to the same partition, which will eventually become a hot spot, making
the data skewed.