package validation

object IsShortNameValid {
    operator fun invoke(cost: String): Boolean {
        val regex = Regex(pattern = "услуги по [а-яА-Я\\d\\s.,]+")
        return regex.matches(input = cost)
    }
}
