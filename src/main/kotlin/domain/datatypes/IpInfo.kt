package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class IpInfo(
    val inn: String,
    val orgn: String,
    val okpo: String,
    val fullNameIp: String,
    val phone: String,
    val email: String
) : FieldData {

}