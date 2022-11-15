import domain.datatypes.*
import domain.documents.DocumentBuilder
import domain.documents.documentSet
import domain.documents.get

val documentSet = documentSet {
    document("/Общий.docx") {
        field("FIRST_NAME", get<RussianFullName>().firstName)
//        field("BIK", get<BankInfo>().bik)
//        field("CORRESPONDENT_ACCOUNT", get<BankInfo>().correspondentAccount)
//        field("BANK_NAME", get<BankInfo>().bankName)
//        field("ACCOUNT_NUMBER", get<BankInfo>().settlementAccountNumber)
    }
    when (get<OrganizationType>()) {
        OrganizationType.IP -> document("/Шаблон договора дл ИП С МЕТКАМИ.docx") {
            commonFields()
            field("ENPREPRENEUR_FIO", get<IpInfo>().fullNameOfHolder) // ФИО предпринимателя ИЗ ОГРНИП
//            field("ENPREPRENEUR_INIC") // Фамилия и  инициалы предпринимателя
//            field("ENPREPRENEUR_INIC_F") // Инициалы и фамилия предпринимателя
            field("OGRNIP_NUMB", get<IpInfo>().ogrn) // ОГРНИП
            field("OGRNIP_DATE", get<IpInfo>().orgrnData) // дата
//            field("PP") // Пункт обоснования закупок
//            field("PURCHASE_RUB_NUMB") // Стоимость закупки цифрами
//            field("PURCHASE_RUB") // Стоимость закупки прописью
//            field("SUMM_COP_NUMB ") // Сумма цифрами в копейках
            field("ENTERPRENEUR_INN", get<IpInfo>().inn) // ИНН ИЗ ОРГНИП
            field("ENTERPRENEUR_WALLET", get<BankInfo>().settlementAccountNumber) // Расчётный счёт
            field("ENTERPRENEUR_BANK", get<BankInfo>().bankName) // Название банка полностью
            field("ENTERPRENEUR_COR_WALLET", get<BankInfo>().correspondentAccount) // Корреспондентский счёт
            field("ENT_BIK", get<BankInfo>().bik) // БИК
            field("ENTERPRENEUR_EMAIL", get<IpInfo>().email) // Адрес электронной почты предпринимателя
            field("ENTERPRENEUR_PHONE", get<IpInfo>().phone) // Номер телефона предпринимателя
        }

        OrganizationType.Ooo -> document("/Шаблон договора для ООО С МЕТКАМИ.docx") {
            commonFields()
            field("CONTRAGENT_FULL_NAME", get<CompanyInfo>().fullNameOfOrg) //Наименование контрагента полностью ИЗ ИНН
//            field("CONTRAGENT_SHORT_NAME") //Наименование контрагента сокращённо ИЗ ИНН
//            field("GENERAL_MANAGER") //Генеральный директор ФИО в именительном падеже полностью
//            field("GENERAL_MANAGER_R") //Генеральный директор ФИО в родительном полностью
//            field("PP") //Пункт обоснования закупок
//            field("PURCHASE_RUB_NUMB") //Стоимость закупки цифрами
//            field("PURCHASE_RUB") //Стоимость закупки прописью
//            field("SUMM_COP_NUMB") //Сумма цифрами  в копейках
            field("CONTRAGENT_ADDRESS", get<CompanyInfo>().location) //Юридический адрес ИЗ ИНН
            field("CONTRAGENT_COR_WALLET", get<BankInfo>().correspondentAccount) //Корреспондентский счёт
            field("INN", get<CompanyInfo>().inn) //ИНН
            field("KPP", get<CompanyInfo>().kpp) //КПП ИЗ ИНН
            field("OGRN", get<CompanyInfo>().ogrn) //ОГРН ИЗ ИНН
            field("CONTRAGENT_WALLET", get<BankInfo>().settlementAccountNumber) //Расчётный счёт
            field("BIK", get<BankInfo>().bik) //БИК
            field("CONTRAGENT_FIO", get<CompanyInfo>().fullNameOfHolder) //ФИО ответсвенного от ООО
            field("CONTRAGENT_PROF", get<CompanyInfo>().post) //Должность ответственного от ООО
            field("CONTRAGENT_EMAIL", get<CompanyInfo>().email) //Адрес электронной почты ответственного от ООО
            field("CONTRAGENT_PHONE", get<CompanyInfo>().phone) //Номер телефона ответственного от ООО
//            field("GENERAL_MANAGER_INIC") //Инициалы и фамилия генерального директора
        }
    }
}

private fun DocumentBuilder.commonFields() {
    field("FULL_NAME", get<RussianFullName>().full())
    field("WITH_INITIALS", get<RussianFullName>().withInitials())
}
