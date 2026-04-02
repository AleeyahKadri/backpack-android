plugins {
    id("org.jetbrains.kotlin.jvm")
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

sourceSets.named("main") {
    java.srcDir("src/main/kotlin")
}

apply(from = "$rootProject.projectDir/kotlin-configuration-check.gradle")
