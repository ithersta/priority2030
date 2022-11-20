package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class FinanciallyResponsiblePerson(
    val fio: String,
    val contactPhoneNumber: String
) : FieldData
