Software usually depends on other libraries to work. Gradle provides APIs to use them in our projects and
also to publish our code to be used in other repositories.

It's important to understand it in order to be able to expose our projects to be reused and also to reuse
libraries across our projects.

## Declaring Dependencies

This is an example of declaration of dependencies:
```kotlin
dependencies {
    implementation("com.google.guava:guava:32.1.2-jre")
    api("org.apache.juneau:juneau-marshall:8.2.0")
}
```

The declaration is composed by 3 components:
- Configuration: represents the scope of the dependency (`implementation`, `api`, etc).
- Module ID: represents what is the dependency module name that will be used.
- Version: represents the version of the dependency.

We can use the repositories block to let Gradle know where to lookup for the dependencies, for example:
```kotlin
repositories {
    google()
    mavenCentral()
}
```

We can depend on modules, projects or files, let's see some examples:
```kotlin
dependencies {
    implementation("org.codehaus.groovy:groovy:3.0.5")
    implementation("org.codehaus.groovy:groovy-json:3.0.5")
    implementation("org.codehaus.groovy:groovy-nio:3.0.5")

    implementation(project(":utils"))
    implementation(project(":api"))

    runtimeOnly(files("libs/a.jar", "libs/b.jar")) // Not recommended
}
```

Gradle imports transitive dependencies from the ones declared in our project.

In order to see all dependencies in our project, we can use the dependencies task:
```bash
$ ./gradlew app:dependencies
```