@file:Suppress("MaxLineLength")

package telegram.resources.strings

import dev.inmo.tgbotapi.utils.bold
import dev.inmo.tgbotapi.utils.buildEntities
import dev.inmo.tgbotapi.utils.regular
import dev.inmo.tgbotapi.utils.regularln
import domain.entities.Email

object Strings {
    object Menu {
        const val Message = "Выберите действие"
        const val CreateDocuments = "Начать заполнять документы"
    }

    object Help {
        const val Back = "вернуться на шаг назад"
    }

    const val InternalError = "Произошла внутренняя ошибка бота"
    const val Welcome =
        "Этот бот поможет Вам с оформлением документов, необходимых для оформления закупки на оказание услуг с ООО или ИП (Закупка у единственного поставщика)"
    const val CreateDocumentsMessage = "Давайте начнём заполнение"
    const val CancelDataCollection = "Прервать заполнение документов?"
    const val CantRollback = "Дальше вернуться нельзя"
    const val PackageDocsReady = "Пакет документов сформирован"
    const val UploadPackageDocs =
        "Проверьте корректность данных в документах. При необходимости внесите изменения перед отправкой на проверку."
    const val Email = "Введите адрес электронной почты"
    const val SuccessfulSendDocsEmail = "Документы успешно отправлены на электронную почту"
    const val SuccessfulSendDocs = "Документы успешно отправлены на проверку"
    const val InProcess = "Функция находится на этапе разработки"
    const val InitiatorFullName = "Введите ФИО инициатора закупки (ответственного исполнителя)"
    const val ReplyToEmail = "Введите адрес электронной почты, на которую необходимо получить ответ о статусе документов"
    const val SendDocuments = "Отправить документы на проверку?"
    val CheckListOfDocs = """|Проверьте список документов:
            |
            |1. Заявка на размещение
            |2. Служебная записка
            |3. Проект договора
            |4. Три коммерческих предложения
            |5. Дополнительные документы — при необходимости. (Например, при закупке услуг по созданию ПО, необходимо подготовить служебную записку)
    """.trimMargin()

    fun commercialOfferPrompt() =
        buildEntities {
            regularln("Для подачи заявки на размещение необходимо подготовить 3 коммерческих предложения (КП). Одно КП от потенциального котрагента. Два КП от других компаний.")
            regularln(" ")
            regularln(
                "КП должно быть оформлено на официальном бланке компании, адресовано для ФГАОУ ВО \"СПбПУ, с подписью и печатью.\n" +
                    "Все 3 КП должны содержать перечень услуг с указанем стоимости за каждую услугу, а также общую сумму услуг (с НДС/без НДС).\n" +
                    "Срок оказания услуг"
            )
            regularln(" ")
            regular("КП ")
            bold("обязательно ")
            regularln("должно содержать:")
            regularln("1. Наименование компании")
            regularln("2. ИНН")
            regularln("3. ОГРН/ОГРНИП")
            regularln("4. Юридический и фактический адрес")
            regularln("5. Адрес электронной почты")
            regularln("6. Телефон")
            regularln("7. КПП, Банковские реквизиты (для ООО)")
        }

    object UploadDocs {
        const val ApplicationForPlacement = "Загрузите заявку на размещение"
        const val OfficialMemo = "Загрузите служебную записку"
        const val DraftAgreement = "Загрузите проект договора"
        const val CommercialOffers = "Загрузите три и более коммерческих предложения"
        const val ExtraDocs = "Загрузите дополнительные документы при необходимости"
    }

    fun incorrectNumOfDocs(current: Int, needed: Int) =
        "Загружено недостаточное количество документов. $current/$needed"

    fun tooBigFileSize(filename: String) = "Файл $filename слишком большой. Загрузите файл размером не более 20Мб"
    fun confirmReplyToEmail(replyTo: Email) = "Вы хотите получить ответ на почту ${replyTo.email}?"
}
