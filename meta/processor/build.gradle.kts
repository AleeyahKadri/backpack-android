plugins {
    kotlin("jvm")
}

dependencies {
    implementation(libs.google.kspApi)
    implementation(libs.square.kotlinPoet)
    implementation(libs.square.kotlinPoetKsp)
    implementation(project(":meta:annotations"))
    testImplementation(libs.test.junit)
    testImplementation(libs.kotlin.compilerTesting)
    testImplementation(libs.kotlin.compilerTestingKsp)
}

sourceSets {
    main {
        java.srcDirs("src/main/kotlin")
    }
}

apply(from = "$rootDir/kotlin-configuration-check.gradle.kts")
