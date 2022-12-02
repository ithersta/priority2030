package parser

import domain.datatypes.BankInfo
import domain.datatypes.IpInfo
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import parser.ConstantsForParsing.time

class ParserBik {
    private lateinit var document: Document
    private val url = "https://bik-info.ru/bik_"
    private var case = 0

    fun parseWebPage(bik: String): BankInfo? {
        val connection = Jsoup
            .connect("$url$bik.html")
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .referrer("https://www.google.com")
            .timeout(time)

        runCatching {
            document = connection.execute().parse()
            val firstStrongElement = document.select("strong").first();
            if (firstStrongElement != null) {
                if (firstStrongElement.text() == "Ошибка!") {
                    return BankInfo("0", "0", "0")
                }
            }
        }.onSuccess {
            case = 1
        }.onFailure {
            when (it) {
                is HttpStatusException -> case = 2
            }
        }
        return (when (case) {
            1 -> BankInfo(bik, corrAccount, bakName)
            2 -> BankInfo("0", "0", "0")
            else -> {
                null
            }
        })
    }

    private val corrAccount: String
        get() {
            val selectorCorrAccount = "body > div.container > ul:nth-child(7) > li:nth-child(2) > b"
            return document.select(selectorCorrAccount).html()
        }
    private val bakName: String
        get() {
            val selectorNameBank = "body > div.container > ul:nth-child(7) > li:nth-child(3) > b"
            return document.select(selectorNameBank).html()
        }
}
