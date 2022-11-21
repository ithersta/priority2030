package domain.datatypes

import domain.entitties.PurchasePoints
import kotlinx.serialization.Serializable

@Serializable
data class PurchasePoint(
    val number: PurchasePoints
):FieldData
