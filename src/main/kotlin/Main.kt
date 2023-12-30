import apps.Configuration
import apps.cards.CardsApplication
import apps.emptyConfig
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import util.createFileIfNotExists
import java.nio.file.Paths

private val prettyJson = Json { prettyPrint = true }

fun main() {
    val userHome = System.getProperty("user.home")
    val rootPath = Paths.get("${userHome}/.ht")

    if (!rootPath.toFile().exists()) {
        val success = rootPath.toFile().mkdirs()
        if (!success) {
            throw RuntimeException("unable to create root $rootPath")
        }
    }

    val configFile = createFileIfNotExists(
        path = rootPath.resolve("config"),
        content = prettyJson.encodeToString(emptyConfig).toByteArray()
    )

    val config = Json.decodeFromString<Configuration>(String(configFile.read()))

    CardsApplication(config.cards).run()
}