package services

import domain.entities.MorphedFullName
import ru.morpher.ws3.ClientBuilder

class Morpher(token: String) {
    private val client = ClientBuilder().useToken(token).build()

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
