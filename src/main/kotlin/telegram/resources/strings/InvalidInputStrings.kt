@file:Suppress("MaxLineLength")

package telegram.resources.strings

object InvalidInputStrings {
    object PurchaseDescription {
        const val InvalidSelectionLetter =
            "Вы ввели некорректные данные. Буква мероприятия представляет собой символ алфавита от а до у. \n" +
                    "Отправьте букву ещё раз"
        const val InvalidSelectionIdentifier =
            "Вы ввели некорректные данные. Показатель представляет собой код из перечня\n" +
                    "Отправьте показатель ещё раз"
    }

    const val InvalidPurchaseCost =
        "Вы ввели некорректные данные. Стоимость закупки необходимо написать в формате рубли.копейки\n" +
                "Например: 120000.00 эквивалентно 120000 рублей 0 копеек"
    const val InvalidPurchasePoint = "Вы ввели некорректные данные. Пункт положения представляет собой цифру\n" +
            "Отправьте номер пункта положения ещё раз"

    const val Invalidfio = "Вы ввели некорректные данные. ФИО необходимо ввести в формате О.А. Евсеева\n" +
            "Отправьте ФИО ещё раз"
    const val InvalidPhoneNumber = "Вы ввели некорректные данные. Номер телефона необходимо " +
            "ввести в формате +78005553536 \n" +
            "Отправьте номер телефона ещё раз"

    const val InvalidEmail = "Вы ввели некорректные данные. Отправьте адрес электронной почты ещё раз."
    const val InvalidDate = "Вы ввели некорректные данные. Формат даты дд.мм.гггг. Отправьте дату ещё раз."
    const val InvalidNumber = "Вы ввели некорректные данные. Отправьте число ещё раз."
    const val InvalidTermOfPayment = "Вы ввели некорректные данные. Возможные способы оплаты:" +
            " аванс 30%, по частям, по факту. Отправьте один из этих вариантов в виде сообщения"
    const val InvalidAnswer = "Вы ввели некорректные данные. " +
            "Ответ должен представлять собой только слова \"Да\" и \"Нет\". " +
            "Отправьте один из этих вариантов в виде сообщения "
}
