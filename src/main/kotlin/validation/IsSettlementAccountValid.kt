package validation

object IsSettlementAccountValid {
    operator fun invoke(paymentAccount: String): Boolean {
        val regex = Regex(pattern = "\\d{20}")
        return regex.matches(input = paymentAccount)
    }
}
