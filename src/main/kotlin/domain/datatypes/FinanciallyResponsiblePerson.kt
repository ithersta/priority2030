package domain.datatypes

import domain.entitties.Fio
import domain.entitties.PhoneNumber
import kotlinx.serialization.Serializable

@Serializable
data class FinanciallyResponsiblePerson(
    val fio: Fio,
    val contactPhoneNumber: PhoneNumber
) : FieldData
