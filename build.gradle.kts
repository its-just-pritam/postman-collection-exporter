plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.10.1"
}

group = "com.postman.collection"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    implementation("foundation.icon:icon-sdk:2.5.0")
    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.25.7")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.1.4")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild.set("221")
        untilBuild.set("232.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    runPluginVerifier {
        ideVersions.set(listOf("2022.1.4"))
    }
}
