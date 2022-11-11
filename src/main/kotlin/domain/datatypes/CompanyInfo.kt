package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class CompanyInfo(
    val inn: String,
    val kpp: String,
    val orgn: String,
    val okpo: String,
    val fullNameOfOrganization: String,
    val fullNameHolder: String,
    val shortNameHolder: String
) : FieldData {

}
