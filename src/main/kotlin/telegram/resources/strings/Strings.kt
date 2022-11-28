@file:Suppress("MaxLineLength")
package telegram.resources.strings

object Strings {
    object Menu {
        const val Message = "Выберите действие"
        const val CreateDocuments = "Начать заполнять документы"
    }

    const val InternalError = "Произошла внутренняя ошибка бота"

    const val Welcome = "Этот бот поможет Вам с оформлением документов, необходимых для оформления закупки на оказание услуг с ООО или ИП (Закупка у единственного поставщика)"
    const val CreateDocumentsMessage = "Давайте начнём заполнение"
    const val PackageDocsReady = "Пакет документов сформирован"
    const val SuccessfulSendDocs = "Документы успешно отправлены на проверку"
    const val InProcess = "Функция находится на этапе разработки"
    const val SendDocuments = "Отправить документы на почту"
    fun checkingListOfDocs():String{
        return(
        """|Проверьте список документов:
            |
            |1. Заявка на размещение
            |2. Служебная записка
            |3. Проект договора
            |4. Три коммерческих предложения
            |5. Дополнительные документы — при необходимости. (Например, при закупке услуг по созданию ПО, необходимо подготовить служебную записку)
            """.trimMargin()
                )
    }

    object UploadDocs {
        const val ApplicationForPlacement = "Загрузите заявку на размещение"
        const val OfficialMemo = "Загрузите служебную записку"
        const val DraftAgreement = "Загрузите проект договора"
        const val CommercialOffers = "Загрузите три и более коммерческих предложения"
        const val ExtraDocs = "Загрузите дополнительные документы при необходимости"
    }

    const val IncorrectNumOfDocs = "Загружено недостаточное количество документов. "
}
