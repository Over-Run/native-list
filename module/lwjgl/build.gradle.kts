plugins {
    `java-test-fixtures`
    id("module-conventions")
}

version = providers.gradleProperty("moduleLwjglVersion").get()

moduleInfoExtension {
    artifactId = "native-list-lwjgl3"
    publicationName = "Native List LWJGL 3 module"
    publicationDescription = "LWJGL 3 module of native-list"
}

dependencies {
    api(project(":"))
    implementation(libs.lwjgl3)
}
