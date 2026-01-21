plugins {
    kotlin("jvm")
}

sourceSets {
    main {
        java.srcDirs("src/main/kotlin")
    }
}

apply(from = "$rootDir/kotlin-configuration-check.gradle.kts")
