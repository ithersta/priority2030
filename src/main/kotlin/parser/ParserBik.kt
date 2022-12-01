package parser

import domain.datatypes.BankInfo
import domain.datatypes.IpInfo
import domain.datatypes.OrganizationType
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import parser.ConstantsForParsing.statusCodeSuccessful
import parser.ConstantsForParsing.time

class ParserBik {
    private lateinit var document: Document
    private val url = "https://bik-info.ru/bik_"

    fun parseWebPage(bik: String): BankInfo? {
        val response = Jsoup.connect("$url$bik.html").timeout(time).execute()
        return if (response.statusCode() == statusCodeSuccessful) {
            document = response.parse()
            if (document.toString().contains("Ошибка!")) {
                BankInfo("0", "0", "0")
            }
            BankInfo(bik, corrAccount, bakName)
        } else {
            null
        }
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
