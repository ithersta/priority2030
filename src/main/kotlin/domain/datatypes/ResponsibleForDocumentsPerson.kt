package domain.datatypes

import domain.entities.Email
import domain.entities.Fio
import domain.entities.PhoneNumber
import kotlinx.serialization.Serializable

@Serializable
data class ResponsibleForDocumentsPerson(
    val fio: Fio,
    val contactPhoneNumber: PhoneNumber,
    val email: Email
) : FieldData
