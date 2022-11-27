// the GraalVM Native Plugin isn't available in Gradle's Plugin Repo yet
pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "tsa-server"

include(
        "domain",
        "signing",
        "datetime",
        "app",
        "web",
        "integration",
        "persistence",
        "test-util"
)
