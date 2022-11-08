package validation

object IsNumberValid {
    operator fun invoke(digit: String): Boolean {
        val regex = Regex(pattern = "(\\d+)")
        return regex.matches(input = digit)
    }
}
