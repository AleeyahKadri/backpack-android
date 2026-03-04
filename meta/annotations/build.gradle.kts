plugins {
    kotlin("jvm")
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDir("src/main/kotlin")
        }
    }
}

apply(from = "$rootDir/kotlin-configuration-check.gradle.kts")
