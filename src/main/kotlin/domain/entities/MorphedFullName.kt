package domain.entities

class MorphedFullName(
    val original: String,
    val name: String,
    val surname: String,
    val patronymic: String,
    val genitive: String
) {
    private val patronymicInitial = patronymic.firstOrNull()?.plus(".").orEmpty()
    private val nameInitial = name.firstOrNull()?.plus(".").orEmpty()
    private val initials get() = "$nameInitial$patronymicInitial"
    val surnameInitials get() = "$surname $initials"
    val initialsSurname get() = "$initials $surname"
}
