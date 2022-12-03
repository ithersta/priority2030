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
        const val Ip = "ИП"
        const val Ooo = "ООО"
        const val Invalid = "Доступные варианты: ИП, ООО"
    }

    object IP {
        const val Inn = "Введите ИНН предпринимателя"
        const val Ogrn = "Введите ОГРН предпринимателя"

        //        const val okpo = "Введите ОКПО предпринимателя"
        const val FullName = "Введите ФИО предпринимателя предпринимателя"
        const val Data = "Введите дату от такого числа ОГРНИП"
        private const val Question = "Вы заключаете договор с этим предпринимателем?"
        const val Yes = "Да"
        const val No = "Нет"
        const val Invalid = "Доступные варианты ответа: Да, Нет"
        const val Location = "Введите юридический адрес предпринимателя"
        const val Phone = "Введите номер телефона предпринимателя"
        const val Email = "Введите адрес электронной почты предпринимателя"
        fun isRight(string: String): String {
            return Question + '\n' + string + '\n'
        }
    }

    object Ooo {
        const val Inn = "Введите ИНН предприятия"
        const val Kpp = "Введите КПП предприятия"
        const val Ogrn = "Введите ОГРН предприятия"

        //        const val okpo = "Введите ОКПО предприятия"
        const val FullNameOfOrg = "Введите название предприятия"
        const val Employee = "Введите ФИО ответсвтенного сотрудника от предприятия"
        const val EmployeeRank = "Введите должность ответсвтенного сотрудника от предприятия"
        const val Location = "Введите юридический адрес предприятия"
        const val Phone = "Введите номер телефона ответственного от предприятия"
        const val Email = "Введите адрес электронной почты ответственного от предприятия"
        private const val Question = "Вы заключаете договор с этим предприятием?"
        const val Yes = "Да"
        const val No = "Нет"
        const val invalid = "Доступные варианты: Да, Нет"
        fun isRight(string: String): String {
            return Question + '\n' + string + '\n'
        }
    }

    object Bank {
        const val Bik = "Введите БИК банка"
        const val CorrAccount = "Введите номер корреспондентский счет"
        const val BankName = "Введите Наименование банк"
        const val Account = "Введите номер расчетного счета"
    }

    object Cost {
        const val Price = "Введите сумму по договору"
        const val IsWrongPrice = Price + ". Например: 150.12"
    }

    object Recommendations {
        const val IsWrongIp = "В базе не найден такой предприниматель"
        const val IsWrongOrg = "В базе не найдена такая организация"
        const val IsWrongBank = "В базе не найдена такой банк"
        const val InnForIp = "Введите корректный ИНН из 12 цифр"
        const val InnForOoo = "Введите корректный ИНН из 10 цифр"
        const val Kpp = "Введите корректный КПП из 9 цифр"
        const val OgrnForIp = "Введите корректный ОГРН из 15 цифр"
        const val OgrnForOoo = "Введите корректный ОГРН из 13 цифр"
        //        const val okpoForIp = "Введите корректный ОКПО из 10 цифр"
        //        const val okpoForOoo = "Введите корректный ОКПО из 8 цифр"
        const val FullName = "Введите корректное ФИО, например: Иванов Иван или Петров Петр Петрович"
        const val Phone = "Введите корректный номер телефона начиная с +7..., например: +79777777777"
        const val Email = "Введите корректную электронную почту"
        const val Bik = "Введите корректный БИК из 9 цифр"
        const val CorrAccount = "Введите корректный корреспондентский счет из 20 цифр"
        const val PaymentAccount = "Введите корректный расчетный счет из 20 цифр"
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
        const val Fio = "Введите инициалы и фамилию материально-ответственного лица\n" +
                "\n" +
                "(Пример: Т.М. Дударенко)"
        const val ContactPhoneNumber =
            "Введите номер мобильного телефона материально-ответственного лица. Формат номера: +79290367458"
    }

    object ResponsibleForDocumentsPerson {
        const val Fio = "Введите инициалы и фамилию ответственного за предоставление отчетных документов\n" +
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

