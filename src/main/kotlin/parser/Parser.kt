package parser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class Parser {
    //     0 -  ООО , 1  - ИП
    private var type: Byte = 0
    private val select = "#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div >"
    private var document: Document? = null
    fun parsing(inn: String): Int {
        try {
            val url = "https://sbis.ru/contragents/"
            val response = Jsoup.connect(url + inn).timeout(10000).execute()
            if (response.statusCode() == 200) {
                document = response.parse()
                type = 1
            }
            return response.statusCode()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    // для ООО у которых есть несколкьо организаций внутри себя
    fun parsing(inn: String, kpp: String): Int {
        try {
            val url = "https://sbis.ru/contragents/"
            val response = Jsoup.connect("$url$inn/$kpp").timeout(10000).execute()
            if (response.statusCode() == 200) {
                document = response.parse()
            }
            return response.statusCode()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    val fullName: String
        get() {
            var fullName = document!!.select(
                select + (" div:nth-child(1) > div.cCard__MainReq >" +
                        " div.ws-flexbox.ws-justify-content-between >" +
                        " div.cCard__MainReq-LeftSide.ws-flexbox.ws-flex-column >" +
                        " div.cCard__MainReq-Left > div.cCard__Director.ws-flexbox" +
                        ".ws-flex-column.ws-flex-wrap.ws-justify" +
                        "-content-start.ws-align-items-start > div > span")
            ).html()
            if (fullName.isEmpty()) {
                fullName = document!!.select(
                    select + (" div:nth-child(1) >" +
                            " div.cCard__MainReq.cCard__MainReq-IP > div.cCard__Name-Addr-IP >" +
                            " div.cCard__MainReq-Name > h1")
                ).html().replace(", ИП".toRegex(), "")
            }
            return fullName
        }
    val shortName: String
        get() {
            val fAndIO = fullName.split(" ".toRegex(), limit = 2).toTypedArray()
            return fAndIO[0] + " " + fAndIO[1].replace("[а-я]+".toRegex(), ".")
        }
    private val mainInfoAboutOrg: Array<String>
        private get() {
            val infoAboutOrg = document!!.select(
                select + " div.cCard__CompanyDescription >" +
                        " p:nth-child(5)"
            ).html().replace("<!-- -->,".toRegex(), "")
            return infoAboutOrg.replace("присвоен".toRegex(), "").split("&nbsp;<!-- -->".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
        }
    val fullNameOfOrg: String
        get() = if (type.toInt() == 0) mainInfoAboutOrg[0] else "ИП " + mainInfoAboutOrg[0]
    val innOfOrg: String
        get() = mainInfoAboutOrg[1]
    val kppOfOrg: String
        get() = mainInfoAboutOrg[2]
    val orgnOfOrg: String
        get() = if (type.toInt() == 0) mainInfoAboutOrg[3] else mainInfoAboutOrg[2]
    val okpoOfOrg: String
        get() = if (type.toInt() == 0) mainInfoAboutOrg[4] else mainInfoAboutOrg[3]
}