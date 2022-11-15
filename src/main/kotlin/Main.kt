import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.bot.settings.limiters.CommonLimiter
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import email.EmailSecrets
import email.EmailSender
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject
import telegram.stateMachine
import java.io.FileInputStream
import java.util.*
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf

suspend fun main() {
    val application = startKoin { modules(priority2030Module) }
    val properties: Properties by application.koin.inject()
    val token = properties.getProperty("TOKEN")
    telegramBot(token) {
        requestsLimiter = CommonLimiter(lockCount = 30, regenTime = 1000)
        client = HttpClient(OkHttp)
    }.buildBehaviourWithLongPolling {
        with(stateMachine) { collect() }
    }.join()
}
