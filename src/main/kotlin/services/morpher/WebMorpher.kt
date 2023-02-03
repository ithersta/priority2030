package services.morpher

import domain.entities.MorphedFullName
import mu.KotlinLogging
import ru.morpher.ws3.ClientBuilder
import java.util.concurrent.ConcurrentHashMap

private val logger = KotlinLogging.logger { }

class WebMorpher(token: String) {
    private val client = ClientBuilder().useToken(token).build()
    private val cache = ConcurrentHashMap<String, MorphedFullName>()

    fun morphFullName(fullName: String): MorphedFullName? = runCatching {
        cache[fullName]?.let { return@runCatching it }
        val declension = client.russian().declension(fullName)
        logger.info {
            runCatching {
                "Morpher queries left for today: ${client.queriesLeftForToday()}"
            }.getOrNull() ?: "Couldn't get morpher queries left for today"
        }
        MorphedFullName(
            original = fullName,
            name = declension.fullName.name,
            surname = declension.fullName.surname,
            patronymic = declension.fullName.patronymic,
            genitive = declension.genitive
        ).also {
            check(it.genitive.isNotBlank())
        }
    }.onSuccess { cache[fullName] = it }.getOrNull()
}
