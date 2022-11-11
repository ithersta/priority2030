package validation

object IsPointNumberValid {
    operator fun invoke(point: String): Boolean {
        val regex = Regex(pattern = "([1-9]|[1-6]\\d|70)")
        return regex.matches(input = point)
    }
}
