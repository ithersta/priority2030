package validation

object IsShortNameValid {
    operator fun invoke(cost: String): Boolean {
        val regex = Regex(pattern = "услуги по .+")
        return regex.matches(input = cost)
    }
}
