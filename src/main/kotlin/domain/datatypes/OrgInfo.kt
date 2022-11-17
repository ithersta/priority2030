package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class OrgInfo(
    val inn: String,
    val kpp: String,
    val ogrn: String,
    val fullNameOfOrg: String,
    val post: String,
    val fullNameOfHolder: String,
    val location: String
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
//    ФИО ГЕНДИРА В РОДИТЕЛЬНОМ ПАДЕЖЕ
//    val fullNameOfHolderInGenitiveCase: String
//        get(){
//
//        }

//    val shortNameOfCompany : String = fullNameOfOrg
}
