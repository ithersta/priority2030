package validation

object IsOgrnipValid {
    operator fun invoke(ogrnip: String): Boolean {
        val regex = Regex(pattern = "\\d{15} от (0[1-9]|[12]\\d|3[01])\\.(0[1-9]|1[012])\\.(19|20)\\d\\d")
        return regex.matches(input = ogrnip)
    }
}

object IsOgrnipValidForIp {
    operator fun invoke(ogrnip: String): Boolean {
        val regex = Regex(pattern = "\\d{15}")
        return regex.matches(input = ogrnip)
    }
}

object IsOgrnipValidForOoo {
    operator fun invoke(ogrnip: String): Boolean {
        val regex = Regex(pattern = "\\d{13}")
        return regex.matches(input = ogrnip)
    }
}