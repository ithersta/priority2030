package domain.entities

import kotlinx.serialization.Serializable
import validation.IsSettlementAccountValid

@Serializable
class SettlementAccount private constructor(
    val value: String
) {
    companion object {
        fun of(value: String) = if (IsSettlementAccountValid(value)) SettlementAccount(value) else null
    }
}
