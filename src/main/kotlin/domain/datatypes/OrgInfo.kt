package domain.datatypes

import domain.entities.Kpp
import domain.entities.MorphedFullName
import domain.entities.OooInn
import kotlinx.serialization.Serializable

@Serializable
data class OrgInfo(
    val inn: OooInn,
    val kpp: Kpp,
    val ogrn: String,
    val abbreviatedNameOfOrg: String,
    val post: String,
    val fullNameOfHolder: MorphedFullName,
    val location: String
) {
    val fullNameOfOrg: String
        get() {
            return abbreviatedNameOfOrg.replace("ООО", "Общество с ограниченной ответственностью")
        }
}
