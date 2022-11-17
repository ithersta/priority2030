package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class BankInfo(
    val bik: String,
    val correspondentAccount: String,
    val bankName: String
) : FieldData
