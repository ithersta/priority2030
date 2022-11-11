@file:Suppress("MaxLineLength")

package telegram.resources.strings

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

    object IP {
        const val inn = "Введите ИНН предпринимателя"
        const val ogrn = "Введите ОГРН предпринимателя"
        const val okpo = "Введите ОКПО  предпринимателя"
        const val fullName = "Введите ФИО предпринимателя предпринимателя"
        const val question = "Вы заключаете договор с этим предпринимателем?"
        const val Yes = "Да"
        const val No = "Нет"
        const val Invalid = "Доступные варианты ответа: Да, Нет"
        const val phone = "Введите номер телефона предпринимателя"
        const val email = "Введите адрес электронной почты предпринимателя"
        fun isRight(string: String): String {
            return question + '\n' + string + '\n' + Invalid
        }
    }

    object Ooo {
        const val inn = "Введите ИНН предприятия"
        const val kpp = "Введите КРР предприятия"
        const val question = "Вы заключаете договор с этим предприятием?"
        const val Yes = "Да"
        const val No = "Нет"
        const val Invalid = "Доступные варианты: Да, Нет"
        const val employee = "Введите ФИО ответсвтенного сотрудника от предприятия"
        const val phone = "Введите номер телефона ответственного от предприятия"
        const val email = "Введите адрес электронной почты ответственного от предприятия"
        fun isRight(string: String): String {
            return question + '\n' + string + '\n' + Invalid
        }
    }

    object Bank {
        const val bik = "Введите БИК банка"
        const val corrAccount = "Введите номер корреспондентский счет"
        const val bankName = "Введите Наименование банк"
        const val account = "Введите номер расчетного счета"
    }
}
