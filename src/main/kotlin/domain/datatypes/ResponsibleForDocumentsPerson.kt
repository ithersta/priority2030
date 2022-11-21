package domain.datatypes

import domain.entitties.Email
import domain.entitties.Fio
import domain.entitties.PhoneNumber
import kotlinx.serialization.Serializable

@Serializable
data class ResponsibleForDocumentsPerson(
    val fio: Fio,
    val contactPhoneNumber: PhoneNumber,
    val email: Email
) : FieldData
