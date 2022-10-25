package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
class RussianFullName(
    val firstName: String,
    val lastName: String,
    val patronymic: String?
) : FieldData {
    fun withInitials() = "${firstName.first()}.${patronymic?.let { " $it." }} $lastName"

    companion object {
        fun of(value: String): RussianFullName? {
            val words = value.split(' ')
            return when (words.size) {
                2, 3 -> RussianFullName(words[0], words[1], words.getOrNull(2))
                else -> null
            }
        }
    }
}
