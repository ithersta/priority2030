package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseObject(
    val shortName: String
) : FieldData