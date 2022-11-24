package domain.datatypes

import domain.entities.Fio
import domain.entities.PhoneNumber
import kotlinx.serialization.Serializable

@Serializable
data class FinanciallyResponsiblePerson(
    val fio: Fio,
    val contactPhoneNumber: PhoneNumber
) : FieldData
