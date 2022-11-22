package parser

import domain.datatypes.IpInfo
import domain.datatypes.OrgInfo
import domain.datatypes.OrganizationType
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import parser.ConstantsForParsing.orderFullName
import parser.ConstantsForParsing.orderInn
import parser.ConstantsForParsing.orderKppOoo
import parser.ConstantsForParsing.orderOgrnIp
import parser.ConstantsForParsing.orderOgrnOoo
import parser.ConstantsForParsing.orderOkpoIp
import parser.ConstantsForParsing.orderOkpoOoo
import parser.ConstantsForParsing.statusCodeSuccessful
import parser.ConstantsForParsing.time
import telegram.resources.strings.CollectorStrings

class Parser {
    private var type: OrganizationType = OrganizationType.Ooo
    private val select = "#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div >"
    private lateinit var document: Document
    fun parsing(inn: String): IpInfo? {
        val url = "https://sbis.ru/contragents/"
        val response = Jsoup.connect(url + inn).timeout(time).execute()
        type = OrganizationType.IP
        return if (response.statusCode() == statusCodeSuccessful) {
            document = response.parse()
            IpInfo(innOfOrg, ogrnOfOrg, fullNameOfHolder, ParserRusprofile().parseWebPage(ogrnOfOrg), location)
        } else {
            null
        }
    }

    // для ООО у которых есть несколкьо организаций внутри себя
    fun parsing(inn: String, kpp: String): OrgInfo? {
        val url = "https://sbis.ru/contragents/"
        val response = Jsoup.connect("$url$inn/$kpp").timeout(time).execute()
        type = OrganizationType.Ooo
        return if (response.statusCode() == statusCodeSuccessful) {
            document = response.parse()
            OrgInfo(innOfOrg, kppOfOrg, ogrnOfOrg, fullNameOfOrg(), post, fullNameOfHolder, location)
        } else {
            null
        }
    }

    private val post: String
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

    private val location: String
        get() {
            val location = document.select(
                "#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div > " +
                        "div:nth-child(1) > div.cCard__Contacts > div.cCard__Contacts-AddressBlock.cCard__" +
                        "Main-Grid-Element > div.cCard__Contacts-Address"
            ).html()
            return location.ifEmpty {
                (document.select(
                    "#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div > " +
                            "div:nth-child(1) > div.cCard__MainReq.cCard__MainReq-IP > " +
                            "div.ws-flexbox.ws-justify-content-between > div.cCard__MainReq-Left > div > " +
                            "div.cCard__Status-Value-IP > div.cCard__MainReq-City.cCard__Contacts-Address"
                ).html())
            }
        }

    private val fullNameOfHolder: String
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
        get() {
            val infoAboutOrg = document.select(
                select + " div.cCard__CompanyDescription >" +
                        " p:nth-child(5)"
            ).html().replace("<!-- -->,".toRegex(), "")
            return infoAboutOrg.replace("присвоен".toRegex(), "").split("&nbsp;<!-- -->".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
        }


    private fun fullNameOfOrg(): String {
        return if (type.equals(OrganizationType.Ooo)) {
            mainInfoAboutOrg[orderFullName]
        } else {
            "ИП " + mainInfoAboutOrg[orderFullName]
        }
    }
    private val innOfOrg: String
        get() = mainInfoAboutOrg[orderInn].replace("ИНН ".toRegex(), "")
    private val kppOfOrg: String
        get() = mainInfoAboutOrg[orderKppOoo].replace("КПП ".toRegex(), "")
    private val ogrnOfOrg: String
        get() = (if (type.equals(CollectorStrings.Ooo)) mainInfoAboutOrg[orderOgrnOoo]
        else mainInfoAboutOrg[orderOgrnIp]).replace("ОГРН ".toRegex(), "")
    private val okpoOfOrg: String
        get() = (if (type.equals(CollectorStrings.Ooo)) mainInfoAboutOrg[orderOkpoOoo]
        else mainInfoAboutOrg[orderOkpoIp]).replace("ОКПО ".toRegex(), "")
}
