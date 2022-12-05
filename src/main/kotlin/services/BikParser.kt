package services

import domain.datatypes.Bank
import domain.entities.Bik
import domain.entities.CorrespondentAccount
import org.jsoup.Jsoup
import services.ConstantsForParsing.Timeout

private const val URL = "https://bik-info.ru/bik_"

class BikParser {
    fun parseWebPage(bik: Bik): Bank? = runCatching {
        val connection = Jsoup
            .connect("$URL${bik.value}.html")
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .referrer("https://www.google.com")
            .timeout(Timeout)

        val document = connection.execute().parse()
        val firstStrongElement = document.select("strong").first();
        check(firstStrongElement?.text() != "Ошибка!")
        val name = document.select("body > div.container > ul:nth-child(7) > li:nth-child(3) > b").html()
        val correspondentAccount = CorrespondentAccount.of(
            document.select("body > div.container > ul:nth-child(7) > li:nth-child(2) > b").html()
        ) ?: error("Correspondent account is invalid")
        Bank(bik, correspondentAccount, name)
    }.getOrNull()
}
