package domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class IpInfo(
    val inn: IpInn,
    val ogrn: IpOgrn,
    val fullNameOfHolder: String,
    val orgrnData: String,
    val location: String
)
