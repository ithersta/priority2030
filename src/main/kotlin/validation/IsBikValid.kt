package validation

object IsBikValid {
    operator fun invoke(bic: String): Boolean {
        val regex = Regex(pattern = "\\d{9}")
        return regex.matches(input = bic)
    }
}
