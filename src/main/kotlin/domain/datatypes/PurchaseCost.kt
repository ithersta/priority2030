package domain.datatypes

import domain.entitties.Numbers
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseCost(
    val costInRubles: Numbers,
    val costInRublesPrescription: String,
    val costInCops: Numbers,
    val costInCopsPrescription: String,
    val rubles:String,
    val cops: String
) : FieldData
