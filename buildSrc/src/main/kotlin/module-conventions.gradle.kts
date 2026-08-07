plugins {
    `java-library`
    `maven-publish`
    signing
}

val moduleInfoExtension = extensions.create<ModuleInfoExtension>("moduleInfoExtension")

group = providers.gradleProperty("projectGroup").get()

repositories {
    mavenCentral()
}

tasks.withType<Javadoc> {
    options {
        jFlags("-Duser.language=en")
    }
}

tasks.withType<JavaCompile> {
    options.release = 25
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }

    withSourcesJar()
    withJavadocJar()
}

publishing.repositories {
    maven {
        name = "staging"
        url = uri(rootProject.layout.buildDirectory.dir("staging-deploy"))
    }
}

afterEvaluate {
    publishing.publications {
        register<MavenPublication>("mavenJava") {
            groupId = project.group.toString()
            artifactId = moduleInfoExtension.artifactId.get()
            version = project.version.toString()
            from(components["java"])
            pom {
                name = moduleInfoExtension.publicationName
                description = moduleInfoExtension.publicationDescription
                setupPom()
            }
        }
    }

    signing {
        sign(publishing.publications["mavenJava"])
    }
}
