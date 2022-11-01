package validation

object IsIndicatorValid {
    operator fun invoke(indicator: String): Boolean {
        val regex = Regex(pattern = "\\d+|-{1}")
        return regex.matches(input = indicator)
    }
}
