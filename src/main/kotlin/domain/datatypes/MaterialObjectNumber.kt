package domain.datatypes

import domain.entitties.Numbers
import kotlinx.serialization.Serializable

@Serializable
data class MaterialObjectNumber(
    val number: Numbers
) : FieldData
