package validation

object IsFullNameValid {
    operator fun invoke(name: String): Boolean {
        // TODO:  нужно будет переписать что-то лучше не нашел  REGEX-а
        val regex = Regex(
            "(^[А-Яа-я]{3,16})([ ]{0,1})([А-Яа-я]{3,16})?([ ]{0,1})?([А-Яа-я]{3,16})?([ ]{0,1})?([А-Яа-я]{3,16})"
        )
        return regex.matches(input = name)
    }
}
