package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class CompanyInfo(
    val inn: String,
    val kpp: String,
    val ogrn: String,
    val fullNameOfOrg: String,
    val post : String,
    val fullNameOfHolder: String,
    val location: String,
    val phone: String,
    val email: String
) : FieldData {

}
