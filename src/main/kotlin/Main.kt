import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.bot.settings.limiters.CommonLimiter
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import org.koin.core.context.startKoin
import telegram.stateMachine

suspend fun main() {
    val application = startKoin { modules(priority2030Module) }
    val mainProperties: MainProperties by application.koin.inject()
    val token = mainProperties.token
    telegramBot(token) {
        requestsLimiter = CommonLimiter(lockCount = 30, regenTime = 1000)
        client = HttpClient(OkHttp)
    }.buildBehaviourWithLongPolling {
        with(stateMachine) { collect() }
    }.join()
}
