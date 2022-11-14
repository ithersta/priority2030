import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.bot.settings.limiters.CommonLimiter
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import email.EmailSecrets
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import telegram.stateMachine
import java.io.FileInputStream
import java.util.*

suspend fun main() {
    val properties = Properties()
    properties.load(FileInputStream(System.getenv()["CONFIG_FILE"] ?: "src/config.cfg"))
    EmailSecrets.hostname = properties.getProperty("EMAIL_HOSTNAME")
    EmailSecrets.port = properties.getProperty("EMAIL_PORT")
    EmailSecrets.username = properties.getProperty("EMAIL_USERNAME")
    EmailSecrets.password = properties.getProperty("EMAIL_PASSWORD")
    EmailSecrets.from = properties.getProperty("EMAIL_FROM")
    val token = properties.getProperty("TOKEN")
    telegramBot(token) {
        requestsLimiter = CommonLimiter(lockCount = 30, regenTime = 1000)
        client = HttpClient(OkHttp)
    }.buildBehaviourWithLongPolling {
        with(stateMachine) { collect() }
    }.join()
}
