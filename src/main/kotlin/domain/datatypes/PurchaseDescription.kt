package domain.datatypes

import domain.entitties.SelectionIdentifier
import domain.entitties.SelectionLetter
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseDescription(
    val shortJustification: String,
    val selectionLetter: SelectionLetter,
    val selectionIdentifier: SelectionIdentifier,
    val fullJustification: String,
    val materialValuesAreNeeded: Boolean
):FieldData
