package validation

object IsShortNameValid {
    operator fun invoke(shortName: String): Boolean {
        val regex = Regex(pattern = "услуги по .+")
        return regex.matches(input = shortName)
    }
}
