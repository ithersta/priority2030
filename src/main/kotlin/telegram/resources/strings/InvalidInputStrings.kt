package telegram.resources.strings


object InvalidInputStrings {
    object PurchaseDescription {
        const val InvalidSelectionLetter =
            "Вы ввели некорректные данные. Буква мероприятия представляет собой символ алфавита от а до у. \n " +
                    "Отправьте букву ещё раз"
        const val InvalidSelectionIdentifier =
            "Вы ввели некорректные данные. Показатель представляет собой номер из перечня\n" +
                    "Отправьте показатель ещё раз"
    }

    const val PurchasePoint = "Вы ввели некорректные данные. Пункт положения представляет собой цифру\n" +
            "Отправьте номер пункта положения ещё раз"

    const val PurchaseIniciator = "Вы ввели некорректные данные. ФИО необзодимо ввести в формате О.А. Евсеева\n" +
            "Отправьте ФИО ещё раз"
}