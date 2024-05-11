# Chapter 10: Batch Processing

In online systems, usually we have a client requesting information and waiting for a server's response. Response time
is important, and we almost always aim to keep it low. Batch processing systems handle a large amount of data and
produces some output. They often take a while and clients are not actively waiting for them.

## Batch Processing with Unix Tools

### The Unix Philosophy

This is a set of principles that guide the design of Unix-like operating systems. The main ideas are:
1. Programs should do **one** thing and do it well.
2. The output of one program should be the input of another.
3. Design programs to be used early.
4. Use tools instead of using unskilled help to solve a problem.

PS: This approach sounds a lot about modern Agile and DevOps practices today, doesn't it?

Example: The `sort` command is a great example of a program that does one thing and does it well. It's also barely
used in isolation, as it's often used in conjunction with other commands.

A Unix shell like `bash` is a great tool to glue these programs together and create powerful data pipelines jobs.

#### Uniform Data Interface

If you want output data to be the input of another program, they need to agree on a common format. In Unix, that
interface is a file, which is an ordered sequence of bytes. Since it's very simple, many different things can be 
represented as it, such as actual text files, a device driver, a network socket, and so on.

By convention, a file is a sequence of bytes as an ASCII text separated by new lines, which is represented as `\n`.
For each line, Unix tools separate them by a delimiter, which is usually a space or a tab. Other tools can use different
delimiters, such as commas for CSV files or pipes for other formats.

#### Separation of Logic and Wiring

Unix tools use their standard input (stdin) and standard output (stdout) to communicate. If we don't specify it, the 
input will be the keyboard and the output will be the screen. However, we can also define files as input and output
or even use **pipes** to connect the output of one program to the input of another.

When writing a program, we don't need to worry about how the input is coming or where the output is going. We just use
the stdin and stout and handle the program's logic. This resembles the **separation of concerns** principle in software
engineering, no? Or even **inversion of control**.

#### Transparency

In general, input files are treated as immutable, so we can run several programs as we want without changing the input
file. We can always write the output of one stage to another job to allow us to reprocess from there later.

The biggest limitation of Unix tools is that they need to run on a single machine. That's where tools like Hadoop come
in. The output files are written only once, without modifying any existing part of an already written file.

## MapReduce and Distributed Filesystems

MapReduce is similar to Unix tools, but it's designed to run on a set of machines. A single MapReduce job is like
a single Unix process: it takes one or more inputs and produces one or more outputs.

MapReduce reads and write jobs from a distributed filesystem, which is called Hadoop Distributed File System (HDFS).
There are other distributed filesystems that can be used, such as Google File System (GFS) and Amazon S3.

HDFS consists of a set of machines running a deamon process. This process exposes a network service that allow other
nodes to access files on that machine. A central server called NameNode keeps track of where each block of data is
stored. In the end, it will be one big filesystem distributed across many machines.

In order to tolerate failures, it replicates data into different machines.

### MapReduce Job Execution

A MapReduce job usually follows these steps:
1. Read a set of input files and break them into records (for example, a log output can be broken into lines).
2. Call the mapper function to perform some operation on each record (for example, extract the timestamp of the log 
line).
3. Sort the records by key.
4. Call the reducer function to perform an operation on the group of records (for example, count).

Steps 1 and 3 are implicit in the MapReduce framework, so we need to implement only the **mapper** and **reducer** 
functions.

The **mapper** function is called once for each record in the input file. It can produce zero or more key-value pairs.
After the MapReduce collects the key-value pairs, it sorts them by key and groups them by key. The **reducer** function
is called once for each key, with the list of values for that key and will produce zero or more output records, for 
example, the number of occurrences of a URL.

The role of the mapper is to transform the input data, while the role of the reducer is to process that data that have
been transformed and sorted.

As we saw, the difference between Unix processing and MapReduce is that the second can run on multiple machines. The
mapper and reduce functions don't need to know where the data is coming from, they just need to process one record at
a time. In Hadoop MapReduce, for example, the mapper and reduce are just Java classes that implement an interface.