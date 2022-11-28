package domain.datatypes

import domain.entities.SelectionIdentifier
import domain.entities.SelectionLetter
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseDescription(
    val shortJustification: String,
    val selectionLetter: SelectionLetter,
    val selectionIdentifier: SelectionIdentifier,
    val fullJustification: String,
    val materialValuesAreNeeded: Boolean
) : FieldData
