package domain.entities

import kotlinx.serialization.Serializable
import validation.IsShortNameValid
import java.util.*

@Serializable
class ShortName private constructor(
    val name: String
) {
    companion object {
        fun of(name: String) = ShortName(name.replaceFirstChar { it.lowercase(Locale.getDefault()) })
            .takeIf { IsShortNameValid(it.name) }
    }
}
