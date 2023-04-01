import com.ithersta.tgbotapi.fsm.engines.regularEngine
import com.ithersta.tgbotapi.persistence.SqliteStateRepository
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.bot.settings.limiters.CommonLimiter
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import mu.KotlinLogging
import org.koin.core.context.startKoin
import telegram.entities.state.DialogState
import telegram.resources.strings.Strings
import telegram.stateMachine

private val logger = KotlinLogging.logger { }

@OptIn(ExperimentalSerializationApi::class)
suspend fun main() {
    val application = startKoin { modules(priority2030Module) }
    val mainProperties: MainProperties by application.koin.inject()
    val token = mainProperties.token
    val engine = stateMachine.regularEngine(
        getUser = { },
        stateRepository = SqliteStateRepository.create<DialogState>(historyDepth = 30, ProtoBuf),
        exceptionHandler = { userId, throwable ->
            logger.info(throwable) { userId }
            sendTextMessage(userId, Strings.InternalError)
        }
    )
    telegramBot(token) {
        requestsLimiter = CommonLimiter(lockCount = 30, regenTime = 1000)
        client = HttpClient(OkHttp)
    }.buildBehaviourWithLongPolling {
        with(engine) { collectUpdates() }
    }.join()
}
