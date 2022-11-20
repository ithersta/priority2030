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
                "Введите краткое описание"
        const val SelectionLetter =
            "Введите букву Мероприятия в целях реализации которых осуществляется закупка, в соответствии с Дорожной картой проекта\n" +
                    "(от а до у)"

        object SelectionIdentifier {
            val SelectionIdentifierOptions= listOf(
                "ПРГ1","ПРГ2","Р1(б)","Р2(б)","Р3(б)","Р4(б)","Р5(б)","Р6(б)","Р1(с1)",
                "Р2(с1)","Р3(с1)","Р4(с1)","Р5(с1)","Р6(с1)","Р7(с1)_1","Р7(с1)_2","Р8(с1)_1","Р8(с1)_2","-"
            )
            const val Question =
                "Выберите из кнопочного меню показатель (код из перечня), в целях реализации которого осуществляется закупка," +
                        " в соответствии с Дорожной картой проекта\n" +
                        "В случае отсутствия, поставьте прочерк."
            const val ClickMe = "Нажмите сюда, чтобы посмотреть перечень показателей"
            const val Link =
                "https://docs.google.com/document/d/1y1Scrg4jyYuLa56ZryV8_HwfCUrmMPEI/edit?usp=sharing&ouid=117136603392830305877&rtpof=true&sd=true"
        }

        const val FullJustification = "Введите ОБОСНОВАНИЕ ЗАКУПКИ\n" +
                "\n" +
                "(Необходимо представить развернутое обоснование закупки с указанием целей закупки и " +
                "ожидаемого результата, описать влияние закупки на задачи и показатели программы «Приоритет-2030»)"
        const val MaterialValuesAreNeeded="Услуги по договору предполагают поставку оборудования/материальных ценностей?"
        const val Yes ="Да"
        const val No="Нет"
    }

    object PurchasePoint {
        const val Question =
            "Введите пункт из Положения о закупках товаров, работ, услуг для нужд ФГАОУ ВО «СПбПУ» " +
                    "на основании которого производится закупка.\n" +
                    "\n" +
                    "Укажите цифру\n"
        const val ClickMe = "Нажмите сюда, чтобы посмотреть положение"
        const val Link =
            "https://docs.google.com/document/d/1M3qR3s7t7c_-QaDK-_JqMsTnQHzhmVWG/edit?usp=sharing&ouid=117136603392830305877&rtpof=true&sd=true"
    }

    const val PurchaseIniciator = "Введите инициалы и фамилию Инициатора закупки (ответственного исполнителя)\n" +
            "\n" +
            "(Пример: О.А. Евсеева)\n"

    const val PurchaseCost = "Введите Стоимость услуг цифрами, отделяя копейки от рублей точкой\n" +
            "\n" +
            "(например: 120000.00 эквивалентно 120000 рублей 0 копеек )"

    object FinanciallyResponsiblePerson {
        const val FIO = "Введите Инициалы и фамилию материально-ответственного лица\n" +
                "\n" +
                "(Пример: Т.М. Дударенко)"
        const val ContactPhoneNumber =
            "Введите номер мобильного телефона материально-ответственного лица. Формат номера: +79290367458"
    }

    object ResponsibleForDocumentsPerson {
        const val FIO = "Введите Инициалы и фамилию ответственного за предоставление отчетных документов\n" +
                "\n" +
                "(Пример: Т.М. Дударенко)"
        const val ContactPhoneNumber =
            "Введите номер мобильного телефона ответственного за предоставление отчетных документов. " +
                    "Формат номера: +79290367458"
        const val Email = "Введите адрес электронной почты ответственного за предоставление отчетных документов."
    }

    const val PurchaseInitiatorDepartment="Введите наименование структурного подразделения для которого осуществляется закупка"
    const val PurchaseDeadline ="Введите сроки поставки товара, выполнения работ, оказания услуг"
    const val PurchaseDeliveryAddress="Введите место поставки товара, выполнения работ, оказания услуг"
    const val MaterialObjectNumber="Введите номер материальной точки"

    object TermOfPayment{
        const val Prepaid="аванс 30%"
        const val Fact="по факту"
        const val Partially="частями"
        const val Question="Выберите способоплаты из кнопочного меню"
    }
}

fun infoWithLink(info: String, linkDescription: String, link: String) =
    buildEntities {
        regularln(info)
        regularln("\n")
        link(linkDescription, link)
    }

