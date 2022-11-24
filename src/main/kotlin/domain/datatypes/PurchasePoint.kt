package domain.datatypes

import domain.entities.PurchasePoints
import kotlinx.serialization.Serializable

@Serializable
data class PurchasePoint(
    val number: PurchasePoints
):FieldData
