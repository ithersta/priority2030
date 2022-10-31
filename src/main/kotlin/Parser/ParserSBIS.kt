package Parser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*

class ParserSBIS {
    fun parser(INN: String) {
        try {
            val url = "https://sbis.ru/contragents/"
            val document: Document = Jsoup.connect(url + INN).get()
            // 0  ФИО полная форма
            var fullName = document.select("#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div > div:nth-child(1) > div.cCard__MainReq > div.ws-flexbox.ws-justify-content-between > div.cCard__MainReq-LeftSide.ws-flexbox.ws-flex-column > div.cCard__MainReq-Left > div.cCard__Director.ws-flexbox.ws-flex-column.ws-flex-wrap.ws-justify-content-start.ws-align-items-start > div > span").html()
            if (fullName.isEmpty()) {
                fullName = document.select("#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div > div:nth-child(1) > div.cCard__MainReq.cCard__MainReq-IP > div.cCard__Name-Addr-IP > div.cCard__MainReq-Name > h1").html().replace(", ИП".toRegex(), "")
            }
            println(fullName)
            // Краткая форма ФИО
            val fAndIO = fullName.split(" ".toRegex(), limit = 2).toTypedArray()
            println(fAndIO[0] + " " + fAndIO[1].replace("[а-я]+".toRegex(), "."))
            val infoAboutOrg = document.select("#container > div.sbis_ru-content_wrapper.ws-flexbox.ws-flex-column > div > div > div.cCard__CompanyDescription > p:nth-child(5)").html().replace("<!-- -->,".toRegex(), "")
            val arr = infoAboutOrg.replace("присвоен".toRegex(), "").split("&nbsp;<!-- -->".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            Arrays.stream(arr).forEach { x: String? -> println(x) }
            // 1
            //            "ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ МОНОЛИТ"
            // 2
            //            "ИНН 7708273103,
            // 3
            //            КПП 781101001,
            // 4
            //            ОГРН 5157746069498,
            // 5
            //            ОКПО 32474633"
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

//ООО case
// Боровской Вадим Александрович
//Боровской В. А.
//ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ "МОНОЛИТ"
//ИНН 7708273103
//КПП 781101001
//ОГРН 5157746069498
//ОКПО 32474633


//ИП case
//Иванова Виктория Ивановна
//Иванова В. И.
//Иванова Виктория Ивановна
//ИНН 723015598341
//ОГРН 322723200028442
//ОКПО 2014761999