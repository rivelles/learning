## Files and Directories

In Unix, we can navigate through the file system. By using the `ls` command, we can list the files and directories in 
the current directory.

The main commands are:
- `ls`: list files and directories.
- `cd`: change directory.
- `pwd`: print working directory.

### The ls command

For `ls`, we can use the following options:
- `-l`: long listing format. This will show more information about the files, such as permissions, owner, group, size
and last modification date.
- `-h`: Show size of the files in human-readable format.
- `-a`: Show all files, including hidden ones (Hidden files start with a dot, usually used for configuration files).
- `-t`: Sort files by modification time.
- `-r`: Reverse the order of the sort. (Useful with `-t` to show the most recent files at the end of the printing).

In the beginning of each line, if the first character is a `d`, it means it's a directory. If it's a `-`, it's a file.

### The cd command

We can move to a different directory by using this command, followed by the path of the directory we want to go to.

We can move to absolute paths starting from the root directory, relative paths from the current directory, and also:
- `.`: Current directory.
- `..`: Parent directory.
- `-`: Previous directory.
- `~`: Home directory.
- `/`: Root directory.

## Unix Filesystem

| Directory | Description                |
|-----------|----------------------------|
| /         | Root directory             |
| /bin      | Commands/Programs          |
| /etc      | Configurations             |
| /home     | Users' home directories    |
| /lib      | System libraries           |
| /tmp      | Temporary files            |
| /usr      | Unix system resources      |
| /var      | Variable system data files |

### Unix files

There are some conventions for file names, such as:
- Prefer lowercase.
- Avoid spaces and symbols.
- Underscores over spaces.
- File extensions are optional.

To read files, we can use, besides text editors, `cat`, which will print its content. To get it paginated, we can use
`less`.