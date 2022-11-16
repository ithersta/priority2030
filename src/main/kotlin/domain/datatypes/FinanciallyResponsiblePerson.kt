package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class FinanciallyResponsiblePerson(
    val FIO: String,
    val contactPhoneNumber: String,
    val workPhoneNumber: String
) : FieldData