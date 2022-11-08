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
        const val phone = "Введите номер телефона предпринимателя"
        const val email = "Введите адрес электронной почты предпринимателя"
    }

    object Ooo {
        const val inn = "Введите ИНН предприятия"
        const val kpp = "Введите КРР предприятия"
        const val employee = "Введите ФИО ответсвтенного сотрудника от предприятия"
        const val phone = "Введите номер телефона ответственного от предприятия"
        const val email = "Введите адрес электронной почты ответственного от предприятия"
    }

    object Bank {
        const val bik = "Введите БИК банка"
        const val account = "Введите расчетный счет"
    }
}
