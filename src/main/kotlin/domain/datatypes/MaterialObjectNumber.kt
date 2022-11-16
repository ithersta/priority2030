package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class MaterialObjectNumber(
    val number: String
) : FieldData