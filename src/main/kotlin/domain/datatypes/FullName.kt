package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
class FullName(
    val firstName: String,
    val lastName: String,
    val patronymic: String?
) : FieldData {
    fun withInitials() = "$lastName ${firstName.first()}. ${patronymic?.first()?.plus(".").orEmpty()}"
}
