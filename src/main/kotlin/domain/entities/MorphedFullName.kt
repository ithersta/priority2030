package domain.entities

import kotlinx.serialization.Serializable

@Serializable
class MorphedFullName(
    val original: String,
    val name: String,
    val surname: String,
    val patronymic: String?,
    val genitive: String
) {
    private val initials get() = "${name.first()}.${patronymic?.let { "${it.first()}." }.orEmpty()}"
    val surnameInitials get() = "$surname $initials"
    val initialsSurname get() = "$initials $surname"
}
