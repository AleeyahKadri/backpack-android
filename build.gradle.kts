/**
 * Backpack for Android - Skyscanner's Design System
 *
 * Copyright 2018 - 2026 Skyscanner Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.ByteArrayOutputStream

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
    dependencies {
        classpath(libs.plugin.android)
        classpath(libs.plugin.kotlin)
        classpath(libs.plugin.detekt)
        classpath(libs.plugin.ksp)
        classpath(libs.plugin.roborazzi)
    }
}

plugins {
    alias(libs.plugins.compose.compiler) apply false
}

apply(from = "publish-root.gradle.kts")

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    repositories {
        google()
        mavenCentral()
    }

    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        config.setFrom(files("$rootDir/.detekt.yml", "$rootDir/.detekt-compose.yml"))
        buildUponDefaultConfig = true
        source.setFrom(files("src", "$rootDir/buildSrc/src"))
    }

    dependencies {
        "detektPlugins"(rootProject.libs.detektRules.compose)
        "detektPlugins"(rootProject.libs.detektRules.formatting)
        "detektPlugins"(rootProject.libs.detektRules.libraries)
    }
}

extra["group"] = "net.skyscanner.backpack"

tasks.register<Copy>("installGitHooks") {
    from(File(rootProject.rootDir, "hooks/pre-commit"))
    into(File(rootProject.rootDir, ".git/hooks"))
    fileMode = 0x1FF // 0775 in octal
}

tasks.register<Exec>("installAiLabels") {
    description = "Install ai-labels for AI tool detection in commits"
    group = "setup"

    workingDir = rootProject.rootDir
    commandLine("bash", "hooks/install-ai-labels.sh")
    isIgnoreExitValue = true
    standardOutput = ByteArrayOutputStream()
    errorOutput = ByteArrayOutputStream()

    doLast {
        val stdout = standardOutput.toString().trim()
        val stderr = errorOutput.toString().trim()
        if (stdout.isNotEmpty()) println(stdout)
        if (stderr.isNotEmpty()) System.err.println(stderr)
    }
}

subprojects {
    afterEvaluate {
        tasks.matching { it.name == "preBuild" }.configureEach {
            dependsOn(rootProject.tasks.named("installGitHooks"), rootProject.tasks.named("installAiLabels"))
        }
    }
}
