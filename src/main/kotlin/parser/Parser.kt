package parser

import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*

class Parser {

    private val mainSelect = "#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div >"
    private val fullNameSelectForCom = " div:nth-child(1) > div.cCard__MainReq > " +
            "div.ws-flexbox.ws-justify-content-between > div.cCard__MainReq-LeftSide.ws-flexbox.ws-flex-column >" +
            " div.cCard__MainReq-Left >" +
            " div.cCard__Director.ws-flexbox.ws-flex-column.ws-flex-wrap.ws-justify-content-start" +
            ".ws-align-items-start > div > span"
    private val fullNameSelectForIP = " div:nth-child(1) > div.cCard__MainReq.cCard__MainReq-IP " +
            "> div.cCard__Name-Addr-IP > div.cCard__MainReq-Name > h1"
    private val data = " div.cCard__CompanyDescription > p:nth-child(5)"

    fun takeDataUsingINN(inn: String) {
        val url = "https://sbis.ru/contragents/"
        val document: Document = try {
            Jsoup.connect(url + inn).get()
        } catch (e: HttpStatusException) {
            throw RuntimeException(e)
        }
        takeContent(document)
    }

    fun takeDataUsingINNAndKPP(inn: String, kpp: String) {
        val url = "https://sbis.ru/contragents/"
        val document: Document = try {
            Jsoup.connect("$url$inn/$kpp").get()
        } catch (e: HttpStatusException) {
            throw RuntimeException(e)
        }
        takeContent(document)
    }

    //  пока что выводит на экран а потом разберемся куда что отправлять
    private fun takeContent(document: Document) {
        var fullName: String = document.select(mainSelect + fullNameSelectForCom).html()
        if (fullName.isEmpty()) {
            fullName = document.select(mainSelect + fullNameSelectForIP).html()
                .replace(", ИП".toRegex(), "")
        }
        println(fullName)
        val fAndIO = fullName.split(" ".toRegex(), limit = 2).toTypedArray()
        println(fAndIO[0] + " " + fAndIO[1].replace("[а-я]+".toRegex(), "."))
        val infoAboutOrg: String =
            document.select(mainSelect + data).html()
                .replace("<!-- -->,".toRegex(), "")
        val arr = infoAboutOrg.replace("присвоен".toRegex(), "").split("&nbsp;<!-- -->".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        Arrays.stream(arr).forEach { x: String? ->
            println(
                x
            )
        }
    }
}
