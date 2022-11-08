package validation

object IsPhoneNumberValid {
    operator fun invoke(phoneNumber: String): Boolean {
        val regex = Regex(pattern = "\\+7\\d{10}")
        return regex.matches(input = phoneNumber)
    }
}
