package services

import domain.entities.MorphedFullName
import ru.morpher.ws3.ClientBuilder
import java.util.concurrent.ConcurrentHashMap

class CachedMorpher(token: String) {
    private val client = ClientBuilder().useToken(token).build()
    private val cache = ConcurrentHashMap<String, MorphedFullName>()

    fun morphFullName(fullName: String): MorphedFullName? = runCatching {
        cache[fullName]?.let { return@runCatching it }
        println(client.queriesLeftForToday())
        val declension = client.russian().declension(fullName)
        MorphedFullName(
            original = fullName,
            name = declension.fullName.name,
            surname = declension.fullName.surname,
            patronymic = declension.fullName.patronymic,
            genitive = declension.genitive
        )
    }.onSuccess { cache[fullName] = it }.getOrNull()
}
