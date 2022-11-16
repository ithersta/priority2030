package parser

import org.jsoup.Jsoup

class ParserRusprofile {
    private val time = 25000
    private val statusCodeSuccessful = 200
    fun parseWebPage(ogrnIp: String): String {
        val url = "https://www.rusprofile.ru/ip/"
        val selectorData = "#ab-test-wrp > div.tile-area.td1 > div > div:nth-child(1) >" +
                " div > div.company-requisites > div:nth-child(2) > dl:nth-child(1) > dd:nth-child(3)"
        val response = Jsoup.connect("$url$ogrnIp").timeout(time).execute()
        if (response.statusCode() == statusCodeSuccessful) {
            return response.parse().select(selectorData).html().replace("от ".toRegex(), "")
        }
        return null.toString()
    }
}
