package domain.datatypes

import domain.entities.Bic
import domain.entities.CorrespondentAccount
import kotlinx.serialization.Serializable

@Serializable
data class Bank(
    val bic: Bic,
    val correspondentAccount: CorrespondentAccount,
    val name: String
)
