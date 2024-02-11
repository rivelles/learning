# Chapter 8 - The Trouble with Distributed Systems

When we work with distributed systems, a lot of things can go wrong. We can even assume that anything that can go wrong
**will** go wrong at some point.

This can include:
- Network problems
- Clocks out of sync
- Software bugs

## Faults and Partial Failures

When we are working with a single computer, we can assume that operations work in a deterministic way. If we have
hardware in perfect conditions, problems are caused by buggy software. And if we face eventual hardware problems, we
stop entirely the system, for example, a memory corruption or a total failure, such as a power outage.

In a distributed system, we no longer operate in an idealized model. Some parts of the system might be working while
others are not. This is called a **partial failure**.

As opposed to running in a single machine, partial failures are **non-deterministic**. You can try to run the same
command multiple times and get different results. We may not even know if a failure has occurred.

When we work with partial failures, fault handling is a key concept that the application needs to be aware of. Usually,
these applications have some common characteristics:
- They are distributed across different machines
- They communicate over a network
- Depending on geographical location, they communicate over a long distance
- They depend on the internet, which might not be 100% reliable

## Unreliable Networks

Distributed systems are built on top of networks, which are not reliable and use the Internet Protocol (IP), which is
asynchronous and unreliable.

When we send a message over the network, we don't know if:
- The recipient is online
- The recipient is reachable
- The recipient received the message and processed it

A lot of things can happen in this process. The remote node may have failed, or the network may have failed. Maybe
the node has processed the message but the response was lost on the way back because of a network failure or even
your own node couldn't receive it.

## Detecting Faults

As we saw, in a distributed environment we need to be able to detect and handle faults.

- A load balancer needs to detect when a server is down and stop sending requests to it
- A database needs to detect if the leader is down and elect a new one

However, these processes are not simple. We can have situations where the node crashed while some process was running,
and we don't necessarily know whether it was successful or not.

We can mitigate some of these issues. For example, TCP protocol acknowledges when packages are received, but we can't
count on it fully. The application might have crashed before processing the message, and the sender will never know.

We can also get error responses and, even if we don't get a response, retry the request a few times, and declare the 
node dead if we don't get a response after a few attempts.

## Timeouts and Unbounded Delays

Timeouts are one of the most used techniques to detect faulty nodes or services. Howver, it's not simple to define them:
- A high timeout can make the system slow to detect faults
- A low timeout can make the system declare a node as faulty when it's not

If a node is declared dead, the system needs to ensure that other nodes receive requests that would be sent to the
dead node. This makes these nodes to start processing more data, if they are already struggling with high load, this
can be problematic.

Declaring a node dead can also make us retry requests to other nodes, and they might end up being processed twice.

A lot of things can happen to make the time for a request to vary:
- If several requests are being sent over the network, they might be queued by the network switch. If the queue is full,
the switch might drop some packages.
- When the packet reaches the recipient, it might be queued again if all CPU cores are busy.
- The OS might pause the process in order to process other tasks, and data might be queued again.
- TCP might apply backpressure to avoid overloading a network link.

TCP will also wait for the packages to be acknowledged, and if it doesn't receive the acknowledgment, it will resend
them all over again. Lost packages are also retransmitted.

In public clouds, resources are shared among different users, and the network might be congested by a "noisy neighbor".