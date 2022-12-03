package domain.datatypes

import domain.entities.Bik
import domain.entities.CorrespondentAccount
import kotlinx.serialization.Serializable

@Serializable
data class Bank(
    val bik: Bik,
    val correspondentAccount: CorrespondentAccount,
    val name: String
)
