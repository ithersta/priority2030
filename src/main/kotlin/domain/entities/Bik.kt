package domain.entities

import kotlinx.serialization.Serializable
import validation.IsBikValid

@Serializable
class Bik private constructor(
    val value: String
) {
    companion object {
        fun of(value: String) = if (IsBikValid(value)) Bik(value) else null
    }
}
