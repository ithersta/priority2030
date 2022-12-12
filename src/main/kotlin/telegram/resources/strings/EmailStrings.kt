package telegram.resources.strings

object EmailStrings {
    object ToBotUser {
        const val Subject = "Ваши сгенерированные документы"
    }

    object ToAdmin {
        fun subject(fullName: String) = "Заявка на услугу. $fullName"
        fun reply(email: String) = "Почта для ответа о статусе документов: $email"
    }
}
