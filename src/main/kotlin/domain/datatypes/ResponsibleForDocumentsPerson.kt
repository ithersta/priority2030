package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class ResponsibleForDocumentsPerson(
    val FIO: String,
    val contactPhoneNumber: String,
    val workPhoneNumber: String,
    val email:String
) : FieldData