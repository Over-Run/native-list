plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.codegenBase)
}

tasks.register<JavaExec>("runGenerator") {
    classpath = sourceSets.main.get().runtimeClasspath
    workingDir = rootDir.resolve("src/main/generated")
    mainClass = "Main"
    args(projectDir.absolutePath)

    doFirst {
        workingDir.mkdirs()
    }
}
