package services

import domain.entities.MorphedFullName
import ru.morpher.ws3.ClientBuilder

// TODO: DI
class Morpher {
    private val client = ClientBuilder().useToken(System.getenv("MORPHER_TOKEN")).build()

    fun morphFullName(fullName: String): MorphedFullName? = runCatching {
        val declension = client.russian().declension(fullName)
        MorphedFullName(
            original = fullName,
            name = declension.fullName.name,
            surname = declension.fullName.surname,
            patronymic = declension.fullName.patronymic,
            genitive = declension.genitive
        )
    }.getOrNull()
}
