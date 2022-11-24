package domain.datatypes

import domain.entities.Numbers
import kotlinx.serialization.Serializable

@Serializable
data class MaterialObjectNumber(
    val number: Numbers
) : FieldData
