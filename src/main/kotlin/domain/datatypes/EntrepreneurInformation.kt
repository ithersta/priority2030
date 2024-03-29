package domain.datatypes

import domain.entities.Email
import domain.entities.IpInfo
import domain.entities.PhoneNumber
import kotlinx.serialization.Serializable

@Serializable
data class EntrepreneurInformation(
    val mainInfo: IpInfo,
    val location: String,
    val phone: PhoneNumber,
    val email: Email
) : FieldData
