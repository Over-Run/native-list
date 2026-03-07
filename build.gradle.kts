plugins {
    `java-library`
    idea
    `maven-publish`
}

group = "io.github.over-run"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("org.jspecify:jspecify:1.0.0")

    testImplementation(platform("org.junit:junit-bom:6.0.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

tasks.withType<Javadoc> {
    options {
        jFlags("-Duser.language=en")
    }
}

sourceSets {
    main {
        java {
            srcDirs("src/main/generated")
        }
    }

}

idea {
    module {
        generatedSourceDirs.add(file("src/main/generated"))
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }

    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    options.release = 25
}

publishing.publications {
    register<MavenPublication>("mavenPublication") {
        groupId = project.group.toString()
        artifactId = "native-list"
        version = project.version.toString()
        from(components["java"])
        pom {
        }
    }
}
