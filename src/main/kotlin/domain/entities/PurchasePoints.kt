package domain.entities

import kotlinx.serialization.Serializable
import validation.IsPointNumberValid

@Serializable
data class PurchasePoints private constructor(
    val point: String
) {
    companion object {
        fun of(point: String) = if (IsPointNumberValid(point)) PurchasePoints(point) else null
    }
}

