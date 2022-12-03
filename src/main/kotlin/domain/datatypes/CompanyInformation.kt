package domain.datatypes

import domain.entities.Email
import domain.entities.MorphedFullName
import domain.entities.OrgInfo
import domain.entities.PhoneNumber
import kotlinx.serialization.Serializable

@Serializable
data class CompanyInformation(
    val mainInfo: OrgInfo,
    val phone: PhoneNumber,
    val email: Email
) : FieldData
