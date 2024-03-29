package domain.entities

data class MorphedFullName(
    val original: String,
    val name: String,
    val surname: String,
    val patronymic: String,
    val genitive: String
) {
    private val patronymicInitial get() = patronymic.firstOrNull()?.plus(".").orEmpty()
    private val nameInitial get() = name.firstOrNull()?.plus(".").orEmpty()
    private val initials get() = "$nameInitial$patronymicInitial"
    val surnameInitials get() = "$surname $initials"
    val initialsSurname get() = "$initials $surname"
}
