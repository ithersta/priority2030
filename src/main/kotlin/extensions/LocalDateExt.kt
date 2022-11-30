package extensions

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.format(pattern: String): String = DateTimeFormatter.ofPattern(pattern).format(toJavaLocalDate())
fun LocalDate.Companion.parse(pattern: String, text: String): LocalDate? = runCatching {
    java.time.LocalDate.parse(text, DateTimeFormatter.ofPattern(pattern)).toKotlinLocalDate()
}.getOrNull()
