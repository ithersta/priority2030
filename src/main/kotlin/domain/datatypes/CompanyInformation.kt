package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class CompanyInformation(
    val mainInfo: OrgInfo,
    val phone: String,
    val email: String
) : FieldData
