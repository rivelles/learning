# Chapter 6 - Partitioning

Partitioning means splitting a database into multiple parts called partitions. Each partition is located on a different
machine. Queries and writes can be routed to the correct partition based on the value of a key, which makes scalability
easier.

Partitioning is usually combined with replication, so that copies of each partition are stored on several machines.

TODO: Add image

## Partitioning of Key-Value Data

Our goal is to split the data evenly across partitions, so that all partitions have roughly the same size. If this 
doesn't happen, we call it **skewed**. A partition with a lot more data than the others is called a **hot spot**.

One way to partition is to assign range of keys to each partition. For example, for an encyclopedia, we could assign
ranges of letters to each partition. This is called **range partitioning**.

TODO: Add image

The downside of this approach is that it can lead to hot spots. In our example, if we have a lot of articles starting
with the letter A, the partition that stores the A range will be a hot spot.