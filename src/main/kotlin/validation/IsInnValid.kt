package validation

object IsInnValidForIp {
    operator fun invoke(inn: String): Boolean {
        val regex = Regex(pattern = "\\d{12}")
        return regex.matches(input = inn)
    }
}

object IsInnValidForOoo {
    operator fun invoke(inn: String): Boolean {
        val regex = Regex(pattern = "\\d{10}")
        return regex.matches(input = inn)
    }
}
