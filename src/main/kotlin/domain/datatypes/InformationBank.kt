package domain.datatypes

import domain.entities.SettlementAccount
import kotlinx.serialization.Serializable

@Serializable
data class InformationBank(
    val bank: Bank,
    val settlementAccount: SettlementAccount
) : FieldData
