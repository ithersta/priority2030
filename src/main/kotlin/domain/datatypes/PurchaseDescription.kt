package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseDescription(
    val shortName: String,
    val shortJustification: String,
    val selectionLetter: String,
    val selectionIdentifier: String,
    val fullJustification: String
):FieldData