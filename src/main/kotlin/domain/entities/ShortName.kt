package domain.entities

import kotlinx.serialization.Serializable
import validation.IsShortNameValid

@Serializable
class ShortName private constructor(
    val name: String
) {
    companion object {
        fun of(name: String) = if (IsShortNameValid(name)) ShortName(name) else null
    }
}
