import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.gradle.internal.tasks.ManagedDeviceInstrumentationTestTask
import net.skyscanner.backpack.screenshots.ScreenshotTestsServer

configure<com.android.build.gradle.internal.dsl.BaseAppModuleExtension> {
    productFlavors {
        create("screenshots") {
            dimension = "version"
            versionNameSuffix = "-screenshots"
            testInstrumentationRunnerArguments["notClass"] = "net.skyscanner.backpack.*"
            testInstrumentationRunnerArguments["class"] = "net.skyscanner.backpack.docs.GenerateScreenshots"
        }
    }
    sourceSets {
        getByName("screenshots") {
            java.srcDirs("src/internal/java")
            res.srcDirs("src/internal/res")
        }
    }
    testOptions {
        animationsDisabled = true
        managedDevices {
            devices {
                create<ManagedVirtualDevice>("Docs") {
                    device = "Pixel"
                    apiLevel = 35
                    systemImageSource = "aosp"
                }
            }
        }
    }
}

val server = ScreenshotTestsServer(rootProject.file("docs"))

tasks.register("startScreenshotsServer") {
    doFirst {
        server.start()
    }
    finalizedBy("stopScreenshotsServer")
}

tasks.register("stopScreenshotsServer") {
    doLast {
        server.close()
    }
}

// disable gradle caching for recording screenshots
tasks.withType<ManagedDeviceInstrumentationTestTask>().configureEach {
    outputs.upToDateWhen { device.get().name != "Docs" }
}

tasks.register("recordScreenshots") {
    mustRunAfter("startScreenshotsServer")
    dependsOn(
        "startScreenshotsServer",
        "DocsScreenshotsDebugAndroidTest"
    )
    finalizedBy("stopScreenshotsServer")
}
