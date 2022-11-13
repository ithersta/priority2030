package parser

import org.jsoup.Jsoup

class ParserRusprofile {
    fun parseWebPage(ogrnIp: String): String {
        val url = "https://www.rusprofile.ru/ip/"
        val selectorData = "#ab-test-wrp > div.tile-area.td1 > div > div:nth-child(1) >" +
                " div > div.company-requisites > div:nth-child(2) > dl:nth-child(1) > dd:nth-child(3)"
        val response = Jsoup.connect("$url$ogrnIp").timeout(10000).execute()
        if (response.statusCode() == 200) {
            val document = response.parse()
            return document.select(selectorData).html()
        }
        return "ДАННЫХ НЕТ!!!"
    }
}

