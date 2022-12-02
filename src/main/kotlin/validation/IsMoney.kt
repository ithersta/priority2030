package validation

object IsMoney {
    operator fun invoke(bic: String): Boolean {
        val regex = Regex(pattern = "^(0|[1-9][0-9]{0,12})(,\\d{3})*(\\.\\d{1,2})?\$")
        return regex.matches(input = bic)
    }
}