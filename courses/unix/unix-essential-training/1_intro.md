## What is Unix?

- It's an OS that was created in the 1960s at AT&T Bell Labs.
- Started as a project of the company called Multics, but it was too complex and expensive so it was decommissioned.
  - The developers decided to create a simpler version of it, which was Unix.
- In 1972 it was rewritten in C, which made it portable to other machines and made its popularity increase a lot.
- AT&T licensed Unix as an open-source software, which made it spread even more.
- There are many open branches of Unix, such as:
  - BSD (Berkeley Software Distribution)
  - Linux
- And there are closed ones, such as:
  - Solaris (Sun/Oracle)
  - AIX (IBM)
  - HP-UX (HP)
- Apple's macOS is considered mixed, since it's based on BSD and has some closed-source components.

## Ways to access Unix

On a macOS and Linux, you can use the terminal. On Windows, you can use WSL (Windows Subsystem for Linux). When we open
a terminal, it logs into Unix and starts a session.

## Unix commands

Commands are divided in three parts:
```
command options arguments
```
- Command is the single word that tells Unix what to do.
- Options are flags that modify the behavior of the command. It can be a single letter with a dash or a word with double 
dashes.
- Arguments are the data that the command will operate on.

Example:
```
ls -l -a -h /home
```
This can be simplified to:
```
ls -lah /home
```

Some options can also receive arguments, for example:
```
banner -w 20 'Hello'
```

## Kernel and Shell

The Kernel is the core of the Unix operating system. It will:
- Manage the allocation of time and memory to programs.

The Shell is a program that runs on top of the Kernel and provides a way to interact with the system. There are many
variations of it, such as `sh`, `bash`, `zsh`, `csh`, `ksh`, and others.

To see which shell you're using, you can run:
```
echo $SHELL
```

We can also switch to different shells, for example in MacOS the default shell program is zsh, but we can switch to bash
simply by running `bash`.

## Unix text editors

The first editor for Unix was `ed`, which was replaced by `vi`. Nowadays, we have `vim` and `emacs` as well.
`vim` is the most popular one, and it's a more powerful version of `vi`.

Another popular program is `nano`, which is simpler and easier to use.