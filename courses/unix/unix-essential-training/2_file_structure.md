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

### Unix directories

To create directories, we can use the `mkdir` command followed by the name of the new directory. If we try to create
it in a path that doesn't exist, we can use the `-p` option to create the parent directories. Example:
```bash
mkdir -p /home/user/non_existing_directory/new_directory
```

To move or rename files and directories, we can use the `mv` command followed by the source and destination. If we 
specify a file name as destination, the file will be renamed. We can use the following options:
- `-i`: Interactive mode. It will ask for confirmation before overwriting a file.
- `-n`: No overwriting.
- `-f`: Force overwriting. (default behavior)

To copy files and directories, we can use the `cp` command followed by the source and destination. However, if we want
to copy directories, Unix will not allow it by default to avoid accidental copies of large amounts of data. To copy a
directory, we need to use the `-R` option, which stands for recursive. Example:
```bash
cp -R /home/user/directory /home/user/new_directory
```

We can also use the `-i`, `-n`, and `-f` options with `cp`.

To delete files and directories, we can use the `rm` command followed by the name of the file or directory. Similarly
to `cp`, Unix will not allow us to delete directories by default, we also need to use the `-R` option. `-f`, `-i`, and
`-n` can also be used with `rm`.

### Symbolic links (symlinks)

Similar to shortcuts or aliases, they can be used to access files and directories quickly. To create a symlink, we can
use the `ln` command followed by the `-s` option, the source, and the symlink name. Example:
```bash
ln -s /home/user/file_name symlink_name
```

A symlink will represent a path to a file, it won't point to the file itself. If the file is deleted or moved, the
symlink will be broken.

A symlynk can be useful in many situations. Imagine we have a directory which contains release files for a program, and
we want to have a way to always get the current release with the same name. Every time a new release is made, we can
update the symlink to point to the new release, and when we access the symlink, we'll always get the latest release.
```
releases/
  release_2024_05_25/
  release_2024_05_23/
  release_2024_05_20/

current_release -> release_2024_05_25
```

### Searching files and directories

To search for files and directories, we can use the `find` command followed by the path to start the search and the
expression to search for. Example:
```bash
find /home -name "vacation_photo.jpg"
```

It allows us to use wildcard characters, such as:
- `*`: Zero or more characters.
- `?`: A single character.
- `[]`: Any character present inside the brackets.

Examples:
Find all files named `file.txt` in the current directory and subdirectories:
```bash
find . -name "file.txt"
```

Find all files with the extension `.jpg` in the home directory:
```bash
find ~ -name "*.jpg"
```

Find all files with the name containing `file`, `mile` or `tile` and a single character before the `.txt` extension in
the root directory:
```bash
find / -name "[fmt]ile_?.txt"
```

We can also search using files metadata, such as owner, file size and last modification date.