package validation

object IsDigitValid {
    operator fun invoke(digit: String): Boolean {
        return try {
            digit.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
}
