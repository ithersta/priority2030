package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseCost(
    val costInRubles: String,
    val costInRublesPrescription: String,
    val costInCops: String,
    val costInCopsPrescription: String
) : FieldData