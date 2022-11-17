package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class EntrepreneurInformation(
    val mainInfo: IpInfo,
    val phone: String,
    val email: String
) : FieldData {

}
