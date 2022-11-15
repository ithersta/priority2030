package parser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class ParserBik {
    private var document: Document? = null
    fun parseWebPage(bik: String): Int {
        val url = "https://bik-info.ru/bik_"
        val response = Jsoup.connect("$url$bik.html").timeout(10000).execute()
        if (response.statusCode() == 200) {
            document = response.parse()
        }
        return response.statusCode()
    }

    val corrAccount: String
        get() {
            val selectorCorrAccount = "body > div.container > ul:nth-child(7) > li:nth-child(2) > b"
            return document!!.select(selectorCorrAccount).html()
        }
    val bakName: String
        get() {
            val selectorNameBank = "body > div.container > ul:nth-child(7) > li:nth-child(3) > b"
            return document!!.select(selectorNameBank).html()
        }
}
