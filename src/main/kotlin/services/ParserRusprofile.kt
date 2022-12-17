package services

import domain.entities.IpOgrn
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.jsoup.Jsoup
import services.ConstantsForParsing.StatusCodeSuccessful
import services.ConstantsForParsing.Timeout
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import java.time.LocalDate as JavaLocalDate

class ParserRusprofile {
    fun parseWebPage(ipOgrn: IpOgrn): LocalDate? {
        val url = "https://www.rusprofile.ru/ip/"
        val selectorData = "#ab-test-wrp > div.tile-area.td1 > div > div:nth-child(1) >" +
            " div > div.company-requisites > div:nth-child(2) > dl:nth-child(1) > dd:nth-child(3)"
        val response = Jsoup.connect("$url${ipOgrn.value}").timeout(Timeout).execute()
        return if (response.statusCode() == StatusCodeSuccessful) {
            val dateTimeFormatter =
                DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.forLanguageTag("ru"))
            val localizedDate = response.parse().select(selectorData).html().replace("от ", "")
            JavaLocalDate.parse(localizedDate, dateTimeFormatter).toKotlinLocalDate()
        } else {
            null
        }
    }
}
