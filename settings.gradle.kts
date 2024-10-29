pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "GradleHarpoon"


plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}