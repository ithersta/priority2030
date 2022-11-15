package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class IpInfo(
    val inn: String,
    val ogrn: String,
    val fullNameOfHolder: String,
    val orgrnData: String,
    val phone: String,
    val email: String
) : FieldData {
    val surnameAfterInitials: String
        get() {
            val fAndIO = fullNameOfHolder.split(" ".toRegex(), limit = 2).toTypedArray()
            return fAndIO[0] + " " + fAndIO[1].replace("[а-я]+".toRegex(), ".")
        }
    val initialsAfterSurname: String
        get() {
            val fAndIO = fullNameOfHolder.split(" ".toRegex(), limit = 2).toTypedArray()
            return fAndIO[1].replace("[а-я]+".toRegex(), ".") + " " + fAndIO[0]
        }
}
