package domain.entities

import domain.entities.Kpp
import domain.entities.MorphedFullName
import domain.entities.OooInn
import domain.entities.OooOgrn
import kotlinx.serialization.Serializable

@Serializable
data class OrgInfo(
    val inn: OooInn,
    val kpp: Kpp,
    val ogrn: OooOgrn,
    val abbreviatedNameOfOrg: String,
    val post: String,
    val fullNameOfHolder: String,
    val location: String
)
