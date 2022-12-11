package domain.entities

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class SelectionLetter private constructor(
    val value: String
) {
    companion object {
        fun of(letter: String) = SelectionLetter(letter.lowercase())
            .takeIf { Regex("[а-уё]").matches(it.value) }
    }
}
