@file:Suppress("MaxLineLength")

package telegram.resources.strings

import dev.inmo.tgbotapi.utils.buildEntities
import dev.inmo.tgbotapi.utils.link
import dev.inmo.tgbotapi.utils.regularln

object CollectorStrings {
    object FullName {
        const val LastName = "Введите фамилию"
        const val FirstName = "Введите имя"
        const val Patronymic = "Введите отчество или нажмите кнопку снизу, если оно отсутствует"
        const val NoPatronymic = "Отсутствует"
    }

    object OrganizationType {
        const val Message = "Введите тип организации"
        const val IP = "ИП"
        const val Ooo = "ООО"
        const val Invalid = "Доступные варианты: ИП, ООО"
    }

    object PurchaseDescription {
        const val ShortName = "Введите краткое наименование предмета закупки\n"
        const val ShortJustification = "Товары или работы/услуги предназначен (-ы) для:\n" +
                "\n" +
                "\"Введите краткое описание\""
        const val SelectionLetter =
            "Введите букву Мероприятия в целях реализации которых осуществляется закупка, в соответствии с Дорожной картой проекта\n" +
                    "(от а до у)"

        object SelectionIdentifier {
            const val Question =
                "Введите Показатель (номер из перечня) в целях реализации которых осуществляется закупка, в соответствии с Дорожной картой проекта\n" +
                        "В случае отсутствия, поставьте прочерк."
            const val ClickMe = "Нажмите сюда, чтобы посмотреть перечень показателей"
            const val Link =
                "https://docs.google.com/document/d/1y1Scrg4jyYuLa56ZryV8_HwfCUrmMPEI/edit?usp=sharing&ouid=117136603392830305877&rtpof=true&sd=true"
        }

        const val FullJustification = "Введите ОБОСНОВАНИЕ ЗАКУПКИ\n" +
                "\n" +
                "(Необходимо представить развернутое обоснование закупки с указанием целей закупки и ожидаемого результата, описать влияние закупки на задачи и показатели программы «Приоритет-2030»)"
    }

    object PurchasePoint {
        const val Question ="\"Введите пункт из Положения о закупках товаров, работ, услуг для нужд ФГАОУ ВО «СПбПУ»\n" +
                "на основании которого производится закупка.\n" +
                "\n" +
                "Укажите цифру\"\n"
        const val ClickMe = "Нажмите сюда, чтобы посмотреть положение"
        const val Link =
            "https://docs.google.com/document/d/1M3qR3s7t7c_-QaDK-_JqMsTnQHzhmVWG/edit?usp=sharing&ouid=117136603392830305877&rtpof=true&sd=true"
    }

    const val PurchaseIniciator = "\"Введите инициалы и фамилию Инициатора закупки (ответственного исполнителя)\n" +
            "\n" +
            "(Пример: О.А. Евсеева)\"\n"

    const val PurchaseCost ="Введите Стоимость услуг цифрами, отделяя копейки от рублей точкой\n" +
            "\n" +
            "(например: 120000.00 эквивалентно 120000 рублей 0 копеек )"
}

fun infoWithLink(info: String, linkDescription: String, link: String) =
    buildEntities {
        regularln(info)
        regularln("\n")
        link(linkDescription, link)
    }

