package domain.datatypes

import kotlinx.serialization.Serializable

sealed interface FullName {
    val value: String

    @Serializable
    data class International(
        override val value: String
    ) : FieldData, FullName

    @Serializable
    data class Russian(
        val lastName: String,
        val firstName: String,
        val patronymic: String?
    ) : FieldData, FullName {
        override val value: String
            get() = "$lastName $firstName $patronymic"

        fun withInitials() = "${firstName.first()}.${patronymic?.let { " $it." }} $lastName"

        companion object {
            fun of(value: String): Russian? {
                val words = value.split(' ')
                return when (words.size) {
                    2, 3 -> Russian(lastName = words[0], firstName = words[1], patronymic = words.getOrNull(2))
                    else -> null
                }
            }
        }
    }
}
