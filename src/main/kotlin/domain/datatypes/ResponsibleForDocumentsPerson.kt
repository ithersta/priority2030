package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class ResponsibleForDocumentsPerson(
    val fio: String,
    val contactPhoneNumber: String,
    val email:String
) : FieldData
