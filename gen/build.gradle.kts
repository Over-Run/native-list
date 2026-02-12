plugins {
    java
}

tasks.register<JavaExec>("runGenerator") {
    classpath = sourceSets.main.get().runtimeClasspath
    workingDir = rootDir.resolve("src/main/generated")
    mainClass = "Main"

    doFirst {
        workingDir.mkdirs()
    }
}
