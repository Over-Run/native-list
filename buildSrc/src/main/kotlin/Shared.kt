import org.gradle.api.publish.maven.MavenPom
import org.gradle.kotlin.dsl.assign

fun MavenPom.setupPom() {
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
