package domain.entities

import kotlinx.serialization.Serializable
import validation.IsBicValid

@Serializable
class Bic private constructor(
    val value: String
) {
    companion object {
        fun of(value: String) = if (IsBicValid(value)) Bic(value) else null
    }
}
