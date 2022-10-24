package validation

object IsIndicatorValid {
    operator fun invoke(indicator: String): Boolean {
        val regex = Regex(pattern = "\\d*|-?")
        return regex.matches(input = indicator)
    }
}
