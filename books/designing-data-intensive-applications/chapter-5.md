# Chapter 5 - Replication

Replication means keeping a copy of some data in a different machine, so that if one machine fails, the data can still 
be served from the remaining one. Also it's useful for scaling reads and keep data geo-distributed.

## Leaders and Followers

This is one of the most common ways to replicate data. The idea is to have one machine acting as the leader. It
receives write operations, write it locally and send the data to the followers with a replication log. The followers
then apply the log locally.

If a client wants to read data, they can go either to the leader or to any follower.

```mermaid
flowchart LR
    A[Client] -->|write| B[Leader]
    B -->|Replication stream| C[Follower 1]
    B -->|Replication stream| D[Follower 2]
```

### Synchronous vs Asynchronous Replication

A write operation can be replicated synchronously or asynchronously.
- **Synchronous**: the leader waits until the follower confirms that it received the write before reporting success 
to the client.
- **Asynchronous**: the leader sends the write to the follower and reports success to the client without waiting for
the follower to confirm.

It's usually impractical to make all followers synchronous, since it would make the write operation too slow and any
outage would make the leader unavailable. So what is usually done is to make one follower synchronous and the others
asynchronous.

### Setting up new followers

When a new follower is added, it needs to copy the data from the leader. This is usually done by:
1. Taking a consistent snapshot of the leader's database.
2. Copying the snapshot to the new follower.
3. The follower connects to the leader and get the replication log since the snapshot was taken.
4. The follower applies the log to the snapshot.

### Handling node outages

#### Follower outage

If a follower is down, it can recover quite easily. It just needs to reconnect to the leader and get the replication
log since the last entry it received.

#### Leader outage

If the leader is down, one of the followers can be promoted to be the new leader. This is called **failover**. It can
happen manually or automatically in the following steps:
1. Detect the leader failure through a heartbeat mechanism.
2. Choose a new leader, which is usually the follower with the most up-to-date data.
3. Set clients to send writes to the new leader.

Some things can go wrong on this process:
- If the leader fails before the followers receive the write, it will be lost.
- Autoincrementing sequences can be a problem, since the new leader may assign the same sequence number to previously
generated values in the old leader. If those values were used in other systems, it can cause conflicts.
- Two nodes may believe they are the leader. Some systems shut down one of the leaders if his is detected.
- Misconfigured timeouts can cause the system to think the leader is down when it's not, making failovers to happen
unnecessarily.