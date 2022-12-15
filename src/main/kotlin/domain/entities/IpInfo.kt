package domain.entities

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class IpInfo(
    val inn: IpInn,
    val ogrn: IpOgrn,
    val fullNameOfHolder: String,
    val ogrnDate: LocalDate
)
