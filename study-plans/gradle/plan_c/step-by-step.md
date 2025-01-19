# Gradle Study - Plan C

For this plan, we need to accomplish the following challenges:
1. Create a project with a single module, a build file and version catalog
2. Apply the java plugin and customize it to enforce a version during compilation
3. Apply other compilation options
4. Configure the main class in the application plugin
5. Apply the spotless plugin to enforce code formatting
6. Apply the checkstyle plugin to ensure code quality
7. Apply the maven-publish plugin to publish artifacts locally

Let's start by creating a new project:
```bash
mkdir more-complex-application
cd more-complex-application
gradle init --type java-application --dsl kotlin
```

Since we are using the application plugin, it will already apply the java plugin by 
default. In `build.gradle.kts` file, we have:

```kotlin
plugins {
    application
}
```

We can see in the Gradle tasks in the IDE that we already have specific java tasks, such as
`compileJava`.

To enforce compiling a specific java version, we need to configure the java toolchain.

A java toolchain is a set of tools to build and run projects, and it's usually provided by
the local environment via JRE or JDK installations. Then it uses the `javac` command to compile,
the `java` command to run commands or tests, and so on.

By default, Gradle would use the same toolchain to run itself and also to build the projects.
Sometimes, this is undesirable because building projects with different versions depending on
the machine can bring unexpected behavior. We might also want to build projects with versions
that Gradle doesn't support.

In order to make building clearer, Gradle allows configuring toolchains on project and task
level.

```kotlin
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
```

With this, Gradle will choose a toolchain based in the requirement we specified (in this case,
using version 21). It will try to find one locally and, if it doesn't find one, it might download
from the toolchain repository. [Here](https://docs.gradle.org/current/userguide/toolchains.html#sub:download_repositories) 
we can see how to configure it.

To test it, let's create a new record (which is available on Java versions >= 14):

```java
record MyRecord(String myVariable) {}
```

Since we have our toolchain configured for version 21, our project will compile just fine.

Now, if we change the toolchain to Java version 8, we can see that the project will not 
compile anymore.

If we run ./gradlew javaToolchains, we can see that we have the versions that Gradle used in our
tests.

```bash
./gradlew javaToolchains

+ Options
     | Auto-detection:     Enabled
     | Auto-download:      Enabled

 + Azul Zulu JDK 1.8.0_432-b06
     | Location:           /Users/lucas.rivelles/.gradle/jdks/azul_systems__inc_-8-aarch64-os_x.2/zulu-8.jdk/Contents/Home
     | Language Version:   8
     | Vendor:             Azul Zulu
     | Architecture:       aarch64
     | Is JDK:             true
     | Detected by:        Auto-provisioned by Gradle
 
 + Eclipse Temurin JDK 21.0.4+7-LTS
     | Location:           /Users/lucas.rivelles/.sdkman/candidates/java/21.0.4-tem
     | Language Version:   21
     | Vendor:             Eclipse Temurin
     | Architecture:       aarch64
     | Is JDK:             true
     | Detected by:        SDKMAN!
```