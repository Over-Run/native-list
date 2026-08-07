import org.gradle.api.provider.Property

interface ModuleInfoExtension {
    val artifactId: Property<String>
    val publicationName: Property<String>
    val publicationDescription: Property<String>
}
