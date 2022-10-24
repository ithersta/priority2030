package validation

object IsInnValid {
    operator fun invoke(inn: String): Boolean {
        val regex = Regex(pattern = "\\d{12}")
        return regex.matches(input = inn)
    }
}