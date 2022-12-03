@file:Suppress("MaxLineLength")

package telegram.resources.strings

import dev.inmo.tgbotapi.utils.buildEntities
import dev.inmo.tgbotapi.utils.link
import dev.inmo.tgbotapi.utils.regularln

object CollectorStrings {
    object OrganizationType {
        const val message = "Введите тип организации"
        const val ip = "ИП"
        const val ooo = "ООО"
        const val invalid = "Доступные варианты: ИП, ООО"
    }

    object IP {
        const val inn = "Введите ИНН предпринимателя"
        const val ogrn = "Введите ОГРН предпринимателя"

        const val fullName = "Введите ФИО предпринимателя предпринимателя"
        const val data = "Введите дату от такого числа ОГРНИП"
        private const val question = "Вы заключаете договор с этим предпринимателем?"
        const val yes = "Да"
        const val no = "Нет"
        const val location = "Введите юридический адрес предпринимателя"
        const val phone = "Введите номер телефона предпринимателя"
        const val email = "Введите адрес электронной почты предпринимателя"

        fun isRight(string: String): String {
            return question + '\n' + string + '\n'
        }
    }

    object Ooo {
        const val inn = "Введите ИНН предприятия"
        const val kpp = "Введите КПП предприятия"
        const val ogrn = "Введите ОГРН предприятия"

        const val fullNameOfOrg = "Введите название предприятия"
        const val employee = "Введите ФИО ответсвтенного сотрудника от предприятия"
        const val employeeRank = "Введите должность ответственного сотрудника от предприятия"
        const val location = "Введите юридический адрес предприятия"
        const val phone = "Введите номер телефона ответственного от предприятия"
        const val email = "Введите адрес электронной почты ответственного от предприятия"
        private const val question = "Вы заключаете договор с этим предприятием?"
        const val yes = "Да"
        const val no = "Нет"
        const val invalid = "Доступные варианты: Да, Нет"

        fun isRight(string: String): String {
            return question + '\n' + string + '\n'
        }
    }

    object Bank {
        const val bik = "Введите БИК банка"
        const val corrAccount = "Введите номер корреспондентский счет"
        const val bankName = "Введите Наименование банк"
        const val account = "Введите номер расчетного счета"
    }

    object Recommendations {
        const val MorpherUnavailable = "Недоступен сервис склонения ФИО"
        const val innForIp = "Введите корректный ИНН из 12 цифр"
        const val innForOoo = "Введите корректный ИНН из 10 цифр"
        const val kpp = "Введите корректный КПП из 9 цифр"
        const val ogrnForIp = "Введите корректный ОГРН из 15 цифр"
        const val ogrnForOoo = "Введите корректный ОГРН из 13 цифр"


        const val fullName = "Введите корректное ФИО, например: Иванов Иван или Петров Петр Петрович"
        const val bik = "Введите корректный БИК из 9 цифр"
        const val corrAccount = "Введите корректный корреспондентский счет из 20 цифр"
        const val paymentAccount = "Введите корректный расчетный счет из 20 цифр"
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
        const val MaterialValuesAreNeeded =
            "Услуги по договору предполагают поставку оборудования/материальных ценностей?"
        const val Yes = "Да"
        const val No = "Нет"
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

    const val PurchaseIniciator = "Введите инициалы и фамилию инициатора закупки (ответственного исполнителя)\n" +
            "\n" +
            "(Пример: О.А. Евсеева)\n"

    object PurchaseCost {
        const val Morpher = "Введите Стоимость услуг цифрами, отделяя копейки от рублей точкой\n" +
                "\n" +
                "(например: 120000.00 эквивалентно 120000 рублей 0 копеек)"
    }

    object FinanciallyResponsiblePerson {
        const val fio = "Введите инициалы и фамилию материально-ответственного лица\n" +
                "\n" +
                "(Пример: Т.М. Дударенко)"
        const val ContactPhoneNumber =
            "Введите номер мобильного телефона материально-ответственного лица. Формат номера: +79290367458"
    }

    object ResponsibleForDocumentsPerson {
        const val fio = "Введите инициалы и фамилию ответственного за предоставление отчетных документов\n" +
                "\n" +
                "(Пример: Т.М. Дударенко)"
        const val ContactPhoneNumber =
            "Введите номер мобильного телефона ответственного за предоставление отчетных документов. " +
                    "Формат номера: +79290367458"
        const val Email = "Введите адрес электронной почты ответственного за предоставление отчетных документов."
    }

    const val PurchaseInitiatorDepartment =
        "Введите наименование структурного подразделения для которого осуществляется закупка"
    const val PurchaseDeadline = "Введите сроки поставки товара, выполнения работ, оказания услуг в формате дд.мм.гггг"
    const val PurchaseDeliveryAddress = "Введите место поставки товара, выполнения работ, оказания услуг"
    const val MaterialObjectNumber = "Введите номер материальной точки"

    object TermOfPayment {
        const val Prepaid = "аванс 30%"
        const val Fact = "по факту"
        const val Partially = "частями"
        const val Question = "Выберите способ оплаты из кнопочного меню"
    }
}

fun infoWithLink(info: String, linkDescription: String, link: String) =
    buildEntities {
        regularln(info)
        regularln("\n")
        link(linkDescription, link)
    }

