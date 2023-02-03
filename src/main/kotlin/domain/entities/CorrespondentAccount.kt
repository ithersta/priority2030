package domain.entities

import kotlinx.serialization.Serializable
import validation.IsCorrAccountValid

@Serializable
class CorrespondentAccount private constructor(
    val value: String
) {
    companion object {
        fun of(value: String) = if (IsCorrAccountValid(value)) CorrespondentAccount(value) else null
    }
}
