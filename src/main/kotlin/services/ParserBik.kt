package services

import domain.datatypes.Bank
import domain.entities.Bic
import domain.entities.CorrespondentAccount
import org.jsoup.Jsoup
import services.ConstantsForParsing.Timeout

private const val URL = "https://bik-info.ru/bik_"

class ParserBik {
    fun parseWebPage(bic: Bic): Bank? = runCatching {
        val connection = Jsoup
            .connect("$URL${bic.value}.html")
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .referrer("https://www.google.com")
            .timeout(Timeout)

        val document = connection.execute().parse()
        val firstStrongElement = document.select("strong").first();
        check(firstStrongElement?.text() != "Ошибка!")
        val name = document.select("body > div.container > ul:nth-child(7) > li:nth-child(3) > b").html()
        val correspondentAccount = CorrespondentAccount.of(
            document.select("body > div.container > ul:nth-child(7) > li:nth-child(2) > b").html()
        )!!
        Bank(bic, correspondentAccount, name)
    }.getOrNull()
}
