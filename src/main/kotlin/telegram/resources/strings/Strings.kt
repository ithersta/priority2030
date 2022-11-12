@file:Suppress("MaxLineLength")
package telegram.resources.strings

object Strings {
    object Menu {
        const val Message = "Выберите действие"
        const val CreateDocuments = "Начать заполнять документы"
    }

    const val Welcome = "Этот бот поможет Вам с оформлением документов, необходимых для оформления закупки на оказание услуг с ООО или ИП (Закупка у единственного поставщика)"
    const val CreateDocumentsMessage = "Давайте начнём заполнение"
    const val InProcess = "Функция находится на этапе разработки"
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
}
