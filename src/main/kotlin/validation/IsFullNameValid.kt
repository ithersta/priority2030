package validation

object IsFullNameValid {
    operator fun invoke(name: String): Boolean {
        val regex = Regex(pattern = "([А-Я]\\.){1,3} [А-Я][а-я]+")
        return regex.matches(input = name)
    }
}
