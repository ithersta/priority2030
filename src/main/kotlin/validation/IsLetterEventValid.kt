package validation

object IsLetterEventValid {
    operator fun invoke(letter: String): Boolean {
        val regex = Regex(pattern = "[а-у]")
        return regex.matches(input = letter)
    }
}