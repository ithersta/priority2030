package validation

object IsKppValid {
    operator fun invoke(kpp: String): Boolean {
        val regex = Regex(pattern = "\\d{9}")
        return regex.matches(input = kpp)
    }
}