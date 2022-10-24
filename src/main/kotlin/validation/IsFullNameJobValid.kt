package validation

object IsFullNameJobValid {
    operator fun invoke(name: String): Boolean {
        val regex = Regex(pattern = "([А-ЯЁ][а-яё]+[\\-\\s]?){2,}[A-Я а-я]+")
        return regex.matches(input = name)
    }
}
