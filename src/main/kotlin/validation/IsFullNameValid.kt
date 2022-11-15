package validation

object IsFullNameValid {
    operator fun invoke(name: String): Boolean {
        val regex = Regex(
            "(^[А-Яа-я]{3,16})([ ]{0,1})([А-Яа-я]{3,16})?([ ]{0,1})?([А-Яа-я]{3,16})?([ ]{0,1})?([А-Яа-я]{3,16})"
        )
        return regex.matches(input = name)
    }
}
