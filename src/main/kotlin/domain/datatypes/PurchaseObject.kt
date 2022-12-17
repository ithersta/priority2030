package domain.datatypes

import domain.entities.ShortName
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseObject(
    val shortName: ShortName
) : FieldData
