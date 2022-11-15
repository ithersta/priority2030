package parser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Parser {
    //     0 -  ООО , 1  - ИП
    private var type: Byte = 0
    private val time = 25000
    private val statusCodeSuccessful = 200
    private val orderFullName = 0
    private val orderInn = 1
    private val orderKppOoo = 2
    private val orderOgrnOoo = 3
    private val orderOkpoOoo = 4
    private val orderOgrnIp = 2
    private val orderOkpoIp = 3
    private val select = "#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div >"
    private lateinit var document: Document
    fun parsing(inn: String): Int {
        val url = "https://sbis.ru/contragents/"
        val response = Jsoup.connect(url + inn).timeout(time).execute()
        if (response.statusCode() == statusCodeSuccessful) {
            document = response.parse()
            type = 1
        }
        return response.statusCode()
    }

    // для ООО у которых есть несколкьо организаций внутри себя
    fun parsing(inn: String, kpp: String): Int {
        val url = "https://sbis.ru/contragents/"
        val response = Jsoup.connect("$url$inn/$kpp").timeout(time).execute()
        if (response.statusCode() == statusCodeSuccessful) {
            document = response.parse()
        }
        return response.statusCode()
    }

    val post: String
        get() {
            val post =
                document.select(
                    "#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div >" +
                            " div:nth-child(1) > div.cCard__MainReq > div.ws-flexbox.ws-justify-content-between >" +
                            " div.cCard__MainReq-LeftSide.ws-flexbox.ws-flex-column > div.cCard__MainReq-Left >" +
                            " div.cCard__Director.ws-flexbox.ws-flex-column.ws-flex-wrap.ws-justify-content-start.ws-" +
                            "align-items-start > div > div > div.cCard__Director-Position"
                ).html()
            return post
        }

    val location: String
        get() {
            val location = document.select(
                "#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div > " +
                        "div:nth-child(1) > div.cCard__Contacts > div.cCard__Contacts-AddressBlock.cCard__" +
                        "Main-Grid-Element > div.cCard__Contacts-Address"
            ).html()
            return location
        }

    val fullNameOfHolder: String
        get() {
            var fullName = document.select(
                select + (" div:nth-child(1) > div.cCard__MainReq >" +
                        " div.ws-flexbox.ws-justify-content-between >" +
                        " div.cCard__MainReq-LeftSide.ws-flexbox.ws-flex-column >" +
                        " div.cCard__MainReq-Left > div.cCard__Director.ws-flexbox" +
                        ".ws-flex-column.ws-flex-wrap.ws-justify" +
                        "-content-start.ws-align-items-start > div > span")
            ).html()
            if (fullName.isEmpty()) {
                fullName = document.select(
                    select + (" div:nth-child(1) >" +
                            " div.cCard__MainReq.cCard__MainReq-IP > div.cCard__Name-Addr-IP >" +
                            " div.cCard__MainReq-Name > h1")
                ).html().replace(", ИП".toRegex(), "")
            }
            return fullName
        }
    private val mainInfoAboutOrg: Array<String>
        private get() {
            val infoAboutOrg = document.select(
                select + " div.cCard__CompanyDescription >" +
                        " p:nth-child(5)"
            ).html().replace("<!-- -->,".toRegex(), "")
            return infoAboutOrg.replace("присвоен".toRegex(), "").split("&nbsp;<!-- -->".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
        }


    val fullNameOfOrg: String
        get() = if (type.toInt() == 0) mainInfoAboutOrg[orderFullName] else "ИП " + mainInfoAboutOrg[orderFullName]
    val innOfOrg: String
        get() = mainInfoAboutOrg[orderInn].replace("ИНН ".toRegex(), "")
    val kppOfOrg: String
        get() = mainInfoAboutOrg[orderKppOoo].replace("КПП ".toRegex(), "")
    val ogrnOfOrg: String
        get() = (if (type.toInt() == 0) mainInfoAboutOrg[orderOgrnOoo] else mainInfoAboutOrg[orderOgrnIp]).replace(
            "ОГРН ".toRegex(), ""
        )
    val okpoOfOrg: String
        get() = (if (type.toInt() == 0) mainInfoAboutOrg[orderOkpoOoo] else mainInfoAboutOrg[orderOkpoIp]).replace(
            "ОКПО ".toRegex(), ""
        )
}
