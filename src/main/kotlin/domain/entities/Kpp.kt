package domain.entities

import kotlinx.serialization.Serializable
import validation.IsKppValid

@Serializable
class Kpp private constructor(
    val value: String
) {
    companion object {
        fun of(value: String) = if (IsKppValid(value)) Kpp(value) else null
    }
}
