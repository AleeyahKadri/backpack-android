import java.util.Properties
import java.io.FileInputStream

extra["githubUsername"] = ""
extra["githubToken"] = ""

val localProps = rootProject.file("local.properties")
if (localProps.exists()) {
    val p = Properties()
    FileInputStream(localProps).use { p.load(it) }
    p.forEach { name, value -> extra[name.toString()] = value }
} else {
    val p = Properties()
    p.putAll(System.getenv())
    p.forEach { name, value ->
        when (name) {
            "GITHUB_USERNAME" -> extra["githubUsername"] = value
            "GITHUB_TOKEN" -> extra["githubToken"] = value
        }
    }
}
