package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class PurchasePoint(
    val number:String
):FieldData