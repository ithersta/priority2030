package domain.entities

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class SelectionLetter private constructor(
    val value: String
) {
    companion object {
        fun of(letter: String): SelectionLetter? {
            val lowercaseLetter = letter.lowercase()
            return SelectionLetter(lowercaseLetter)
                .takeIf { Regex("[а-уё]").matches(lowercaseLetter) }
        }
    }
}
