import org.jreleaser.model.Active

plugins {
    `java-library`
    idea
    `maven-publish`
    id("org.jreleaser") version "1.23.0"
}

group = "io.github.over-run"
version = "1.1.0"

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

publishing.repositories {
    maven {
        name = "staging"
        url = uri(layout.buildDirectory.dir("staging-deploy"))
    }
}

publishing.publications {
    register<MavenPublication>("mavenPublication") {
        groupId = project.group.toString()
        artifactId = "native-list"
        version = project.version.toString()
        from(components["java"])
        pom {
            name = "Native List"
            description = "Native list is a resizable array backed by memory segment."
            url = "https://github.com/Over-Run/native-list"
            licenses {
                license {
                    name = "MIT License"
                    url = "https://raw.githubusercontent.com/Over-Run/native-list/refs/heads/main/LICENSE"
                }
            }
            developers {
                developer {
                    name = "squid233"
                    organization = "Overrun Organization"
                    organizationUrl = "https://github.com/Over-Run"
                }
            }
            scm {
                connection = "scm:git:git://github.com/Over-Run/native-list.git"
                developerConnection = "scm:git:ssh://github.com:Over-Run/native-list.git"
                url = "https://github.com/Over-Run/native-list"
            }
        }
    }
}

jreleaser {
    signing {
        pgp {
            active = Active.ALWAYS
            armored = true
        }
    }
    deploy {
        maven {
            mavenCentral {
                mavenCentral {
                    register("release-deploy") {
                        active = Active.RELEASE
                        url = "https://central.sonatype.com/api/v1/publisher"
                        stagingRepository("build/staging-deploy")
                    }
                }
                nexus2 {
                    register("snapshot-deploy") {
                        active = Active.SNAPSHOT
                        snapshotUrl = "https://central.sonatype.com/repository/maven-snapshots/"
                        applyMavenCentralRules = true
                        snapshotSupported = true
                        closeRepository = true
                        releaseRepository = true
                        stagingRepository("build/staging-deploy")
                    }
                }
            }
        }
    }
}
