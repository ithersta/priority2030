package telegram.resources.strings

object EmailStrings {
    object ToBotUser {
        const val Subject = "Ваши сгенерированные документы"
        const val Message = ""
    }

    object ToAdmin {
        fun subject(fullName: String) = "Заявка на услугу. $fullName"
        const val Message = ""
    }
}
