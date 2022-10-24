package validation

object IsCorrAccountValid {
    operator fun invoke(corrAccount: String): Boolean {
        val regex = Regex(pattern = "301\\d{17}")
        return regex.matches(input = corrAccount)
    }
}