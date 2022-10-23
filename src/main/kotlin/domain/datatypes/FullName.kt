package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
class FullName(
    val firstName: String,
    val lastName: String,
    val middleName: String?
): FieldData {
    fun withInitials() = "$lastName ${firstName.first()}. ${middleName?.first()?.plus(".") ?: ""}"
}
