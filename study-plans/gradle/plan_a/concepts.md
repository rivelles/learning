# Gradle Study - Plan A

_"I need to be able to explain the main concepts of Gradle"_

Main source: [Gradle documentation](https://docs.gradle.org/current/userguide/userguide.html)

## What is Gradle?

Gradle is the most popular build tool for JVM-based applications. It's tha default ecosystem to Android
and Kotlin multiplatform projects. We can automate a wide range of build scenarios with built-in functionality,
custom logic and plugins.

It provides optimizations, such as:
- Caching
- Parallelization

It provides a tool called **build scan** that helps to provide insights about our builds.

## Core Concepts

![img.png](img.png)

**Projects**: Piece of software that can be built. It can be:
- Single project: includes only one project called the root project.
- Multi-project: includes one root project and multiple subprojects.

**Build scripts**: Detail what steps to take to build the project.

**Dependency management**: Declares and resolves external dependencies needed by the project.

**Tasks**: A single unit of work that is executed while building the project.

**Plugins**: Extend capabilities and add different **tasks** to a project.

## Running gradle

The preferred way to run Gradle is by using gradle wrapper, which is a portable version of gradle,
so there is no need to install it. When running, it will invoke a declared version of gradle, downloading
it if necessary.

To build a project, we just run:
```commandline
$ ./gradlew build
```
To execute a task called `taskName` with an `exampleOption`, we can just run:
```commandline
$ ./gradlew :taskName --exampleOption=exampleValue
```
## Settings file

The main purpose of this file is to declare subprojects to our build. If our project is a single-project,
this file is optional. This file is located in the root directory of our project, and it's called `settings.gradle` or
`settings.gradle.kts`, depending on which language we chose (Groovy or Kotlin).

```kotlin
rootProject.name = "root-project"   

include("sub-project-a")            
include("sub-project-b")
include("sub-project-c")
```

## Build file

Details the build configuration, tasks and plugins.

There, we can declare two types of dependencies:
1. The ones that Gradle and the build script depend on.
2. The ones that the project's source code depends.

### Adding Plugins

To add a plugin to a build file, we declare it inside the `plugins` node:
```kotlin
plugins {
    id("application")
}
```

The `application` plugin facilitates the creation of a JVM application. By applying it, we automatically
apply the `java` plugin as well, which adds Java compilation along with testing and
bundling (bundle dependencies to the project) capabilities to the project.

It also provides us a way of declaring the main class, which is used to run the application:
```kotlin
application {
    mainClass = "com.example.Main"
}
```

## Dependency Management

Automated technique to resolve dependencies that are used by the project.

### Version catalog

A way to centralize dependency declarations in a shared file called `libs.versions.toml`. It makes sharing
dependencies among subprojects easily.
```toml
[versions]
androidGradlePlugin = "7.4.1"
mockito = "2.16.0"

[libraries]
googleMaterial = { group = "com.google.android.material", name = "material", version = "1.1.0-alpha05" }
mockitoCore = { module = "org.mockito:mockito-core", version.ref = "mockito" }

[plugins]
androidApplication = { id = "com.android.application", version.ref = "androidGradlePlugin" }
```

The file is located into the `gradle/` directory.

