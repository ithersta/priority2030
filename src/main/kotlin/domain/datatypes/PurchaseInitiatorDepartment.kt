package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseInitiatorDepartment(
    val department: String
) : FieldData
