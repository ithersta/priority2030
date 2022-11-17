package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class InformationBank(
    val mainInfo: BankInfo,
    val settlementAccountNumber: String
) : FieldData
