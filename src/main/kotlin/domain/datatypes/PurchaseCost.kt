package domain.datatypes

data class PurchaseCost(
    val costInRubles: Integer,
    val costInRublesPrescription: String,
    val costInCops: Integer,
    val costInCopsPrescription: String
) : FieldData