package validation

object IsPurchaseCostValid {
    operator fun invoke(cost: String): Boolean {
        val regex = Regex(pattern = "\\d+\\.\\d\\d")
        return regex.matches(input = cost)
    }
}
