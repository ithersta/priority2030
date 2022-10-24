package validation

//насколько я помню, в первом боте мы использовали что-то готовое
object IsEmailValid {
    operator fun invoke(email: String): Boolean {
        val regex = Regex(pattern = "([a-zA-Z\\d._-]+@[a-zA-Z\\d._-]+\\.[a-zA-Z\\d_-]+)")
        return regex.matches(input = email)
    }
}
