package services

import domain.datatypes.IpInfo
import domain.datatypes.OrgInfo
import domain.entities.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import services.ConstantsForParsing.Timeout
import services.ConstantsForParsing.orderFullName
import services.ConstantsForParsing.orderOgrnIp
import services.ConstantsForParsing.orderOgrnOoo

private const val URL = "https://sbis.ru/contragents/"
private const val SELECT = "#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div >"

class Parser {
    fun getIpInfo(inn: IpInn): IpInfo? = runCatching {
        val connection = Jsoup
            .connect("$URL${inn.value}")
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .referrer("https://www.google.com")
            .timeout(Timeout)
        val document = connection.execute().parse()
        val mainInfoAboutOrg = mainInfoAboutOrg(document)
        val ipOgrn = IpOgrn.of(mainInfoAboutOrg[orderOgrnIp].replace("ОГРН ", ""))!!
        IpInfo(
            inn,
            ipOgrn,
            fullNameOfHolder(document),
            ParserRusprofile().parseWebPage(ipOgrn)!!,
            location(document)
        )
    }.getOrNull()

    fun getOrgInfo(inn: OooInn, kpp: Kpp): OrgInfo? = runCatching {
        val connection = Jsoup
            .connect("$URL${inn.value}/${kpp.value}")
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .referrer("https://www.google.com")
            .timeout(Timeout)
        val document = connection.execute().parse()
        val mainInfoAboutOrg = mainInfoAboutOrg(document)
        val fullNameOfOrg = mainInfoAboutOrg[orderFullName]
        val ogrnOfOrg = OooOgrn.of(mainInfoAboutOrg[orderOgrnOoo].replace("ОГРН ", ""))!!
        OrgInfo(
            inn,
            kpp,
            ogrnOfOrg,
            fullNameOfOrg,
            post(document),
            Morpher().morphFullName(fullNameOfHolder(document))!!,
            location(document)
        )
    }.getOrNull()

    private fun post(document: Document) = document.select(
        "#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div >" +
                " div:nth-child(1) > div.cCard__MainReq > div.ws-flexbox.ws-justify-content-between >" +
                " div.cCard__MainReq-LeftSide.ws-flexbox.ws-flex-column > div.cCard__MainReq-Left >" +
                " div.cCard__Director.ws-flexbox.ws-flex-column.ws-flex-wrap.ws-justify-content-start.ws-" +
                "align-items-start > div > div > div.cCard__Director-Position"
    ).html()

    private fun location(document: Document) = document.select(
        "#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div > " +
                "div:nth-child(1) > div.cCard__Contacts > div.cCard__Contacts-AddressBlock.cCard__" +
                "Main-Grid-Element > div.cCard__Contacts-Address"
    ).html().ifEmpty {
        (document.select(
            "#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div > " +
                    "div:nth-child(1) > div.cCard__MainReq.cCard__MainReq-IP > " +
                    "div.ws-flexbox.ws-justify-content-between > div.cCard__MainReq-Left > div > " +
                    "div.cCard__Status-Value-IP > div.cCard__MainReq-City.cCard__Contacts-Address"
        ).html())
    }

    private fun fullNameOfHolder(document: Document) = document.select(
        SELECT + (" div:nth-child(1) > div.cCard__MainReq >" +
                " div.ws-flexbox.ws-justify-content-between >" +
                " div.cCard__MainReq-LeftSide.ws-flexbox.ws-flex-column >" +
                " div.cCard__MainReq-Left > div.cCard__Director.ws-flexbox" +
                ".ws-flex-column.ws-flex-wrap.ws-justify" +
                "-content-start.ws-align-items-start > div > span")
    ).html().ifEmpty {
        document.select(
            SELECT + (" div:nth-child(1) >" +
                    " div.cCard__MainReq.cCard__MainReq-IP > div.cCard__Name-Addr-IP >" +
                    " div.cCard__MainReq-Name > h1")
        ).html().replace(", ИП", "")
    }

    private fun mainInfoAboutOrg(document: Document): Array<String> {
        val infoAboutOrg = document.select(
            SELECT + " div.cCard__CompanyDescription >" +
                    " p:nth-child(5)"
        ).html().replace("<!-- -->,".toRegex(), "")
        return infoAboutOrg.replace("присвоен".toRegex(), "").split("&nbsp;<!-- -->".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
    }
}
