package services

import org.jsoup.Jsoup
import services.ConstantsForParsing.StatusCodeSuccessful
import services.ConstantsForParsing.Timeout

class ParserRusprofile {
    fun parseWebPage(ogrnIp: String): String? {
        val url = "https://www.rusprofile.ru/ip/"
        val selectorData = "#ab-test-wrp > div.tile-area.td1 > div > div:nth-child(1) >" +
                " div > div.company-requisites > div:nth-child(2) > dl:nth-child(1) > dd:nth-child(3)"
        val response = Jsoup.connect("$url$ogrnIp").timeout(Timeout).execute()
        return if (response.statusCode() == StatusCodeSuccessful) {
            response.parse().select(selectorData).html().replace("от ", "")
        } else {
            null
        }
    }
}
