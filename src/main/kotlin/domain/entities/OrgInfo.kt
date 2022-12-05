package domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class OrgInfo(
    val inn: OooInn,
    val kpp: Kpp,
    val ogrn: OooOgrn,
    val fullName: String,
    val post: String,
    val fullNameOfHolder: String,
    val location: String
) {
    val shortName get() = fullName.replace("общество с ограниченной ответственностью", "ООО", ignoreCase = true)
}
