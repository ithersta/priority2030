import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.bot.settings.limiters.CommonLimiter
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import ru.morpher.ws3.ClientBuilder
import telegram.stateMachine
import java.io.File
import kotlin.system.exitProcess

suspend fun main() {
    val morpherToken = System.getenv("MORPHER_TOKEN")
    val client = ClientBuilder()
        .useToken(morpherToken)
        .build()
    println(client.queriesLeftForToday())
    client.russian().declension("Лысенко Александра Владимировна").run {
        println(nominative)
        println(dative)
        println(genitive)
        println(accusative)
        println(instrumental)
        println(prepositional)
        println(fullName.name)
        println(fullName.surname)
        println(fullName.patronymic)
    }
    client.russian().spell(300, "рубль").run {
        println("${numberDeclension.nominative} ${unitDeclension.nominative}")
    }
    exitProcess(0)

    val token = System.getenv()["TOKEN"] ?: File(System.getenv("TOKEN_FILE")).readText()
    telegramBot(token) {
        requestsLimiter = CommonLimiter(lockCount = 30, regenTime = 1000)
        //client = HttpClient(OkHttp)
    }.buildBehaviourWithLongPolling {
        with(stateMachine) { collect() }
    }.join()
}
