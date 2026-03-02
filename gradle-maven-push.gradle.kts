import org.gradle.api.publish.maven.MavenPublication
import java.net.URI

plugins {
    id("maven-publish")
}

configure<com.android.build.api.dsl.LibraryExtension> {
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

afterEvaluate {
    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = URI("https://maven.pkg.github.com/skyscanner/backpack-android")
                credentials {
                    username = if ((rootProject.extra["githubUsername"] as String).isEmpty()) {
                        System.getenv("GITHUB_ACTOR")
                    } else {
                        rootProject.extra["githubUsername"] as String
                    }
                    password = if ((rootProject.extra["githubToken"] as String).isEmpty()) {
                        System.getenv("GITHUB_TOKEN")
                    } else {
                        rootProject.extra["githubToken"] as String
                    }
                }
            }
        }

        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.extra["group"] as String
                artifactId = project.extra["artifactId"] as String
                version = project.version as String

                from(components["release"])

                pom {
                    name.set(project.extra["artifactId"] as String)
                    description.set("Backpack is a collection of design resources, reusable components and guidelines for creating Skyscanner's products.")
                    url.set("https://github.com/Skyscanner/backpack-android")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("https://github.com/Skyscanner/backpack-android/blob/main/LICENSE.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("skyscanner")
                            name.set("Skyscanner Open Source")
                            email.set("koalasquad@skyscanner.net")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/Skyscanner/backpack-android.git")
                        developerConnection.set("scm:git:ssh://github.com/Skyscanner/backpack-android.git")
                        url.set("http://github.com/Skyscanner/backpack-android/tree/main")
                    }
                }
            }
        }
    }
}
