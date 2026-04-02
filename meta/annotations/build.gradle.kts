plugins {
    id("org.jetbrains.kotlin.jvm")
}

sourceSets.named("main") {
    java.srcDir("src/main/kotlin")
}

apply(from = "$rootProject.projectDir/kotlin-configuration-check.gradle")
