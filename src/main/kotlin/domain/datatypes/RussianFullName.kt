package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class RussianFullName(
    val lastName: String,
    val firstName: String,
    val patronymic: String?
) : FieldData {
    fun full() = "$lastName $firstName${patronymic?.let { " $it" }.orEmpty()}"
    fun withInitials() = "${firstName.first()}.${patronymic?.first()?.let { "$it." }.orEmpty()} $lastName"
}
