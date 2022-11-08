package validation

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat


object IsDateValid {
    operator fun invoke(data: String): Boolean {
        val pattern = "dd.MM.yyyy"
        val date =  SimpleDateFormat(pattern).parse("01.01.2022")
        return try {
            val df: DateFormat = SimpleDateFormat(pattern)
            df.isLenient = false
            df.parse(data).after(date)
        } catch (e: ParseException) {
            false
        }
    }
}
