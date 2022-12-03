package domain.datatypes

import domain.entities.IpInn
import domain.entities.IpOgrn
import kotlinx.serialization.Serializable

@Serializable
data class IpInfo(
    val inn: IpInn,
    val ogrn: IpOgrn,
    val fullNameOfHolder: String,
    val orgrnData: String,
    val location: String
) {
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
