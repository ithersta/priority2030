package validation

object IsDigit {
    operator fun invoke(digit: String): Boolean {
        return try {
            digit.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
}
