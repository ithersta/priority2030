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
            field("ENPREPRENEUR_FIO", get<EntrepreneurInformation>().mainInfo.fullNameOfHolder) // ФИО предпринимателя ИЗ ОГРНИП
            field("ENPREPRENEUR_INIC", get<EntrepreneurInformation>().mainInfo.surnameAfterInitials) // Фамилия и  инициалы предпринимателя
            field("ENPREPRENEUR_INIC_F", get<EntrepreneurInformation>().mainInfo.initialsAfterSurname) // Инициалы и фамилия предпринимателя
            field("OGRNIP_NUMB", get<EntrepreneurInformation>().mainInfo.ogrn) // ОГРНИП
            field("OGRNIP_DATE", get<EntrepreneurInformation>().mainInfo.orgrnData) // дата
//            field("PP") // Пункт обоснования закупок
//            field("PURCHASE_RUB_NUMB") // Стоимость закупки цифрами
//            field("PURCHASE_RUB") // Стоимость закупки прописью
//            field("SUMM_COP_NUMB ") // Сумма цифрами в копейках
            field("ENTERPRENEUR_INN", get<EntrepreneurInformation>().mainInfo.inn) // ИНН ИЗ ОРГНИП
            field("ENTERPRENEUR_WALLET", get<InformationBank>().settlementAccountNumber) // Расчётный счёт
            field("ENTERPRENEUR_BANK", get<InformationBank>().mainInfo.bankName) // Название банка полностью
            field("ENTERPRENEUR_COR_WALLET", get<InformationBank>().mainInfo.correspondentAccount) // Корреспондентский счёт
            field("ENT_BIK", get<InformationBank>().mainInfo.bik) // БИК
            field("ENTERPRENEUR_EMAIL", get<EntrepreneurInformation>().email) // Адрес электронной почты предпринимателя
            field("ENTERPRENEUR_PHONE", get<EntrepreneurInformation>().phone) // Номер телефона предпринимателя
        }

        OrganizationType.Ooo -> document("/Шаблон договора для ООО С МЕТКАМИ.docx") {
            commonFields()
            field("CONTRAGENT_FULL_NAME", get<CompanyInformation>().mainInfo.fullNameOfOrg) //Наименование контрагента полностью ИЗ ИНН
//            field("CONTRAGENT_SHORT_NAME") //Наименование контрагента сокращённо ИЗ ИНН
            field("GENERAL_MANAGER", get<CompanyInformation>().mainInfo.fullNameOfHolder) //Генеральный директор ФИО в именительном падеже полностью
//            field("GENERAL_MANAGER_R",get<CompanyInfo>().fullNameOfHolderInGenitiveCase) //Генеральный директор ФИО в родительном полностью
//            field("PP") //Пункт обоснования закупок
//            field("PURCHASE_RUB_NUMB") //Стоимость закупки цифрами
//            field("PURCHASE_RUB") //Стоимость закупки прописью
//            field("SUMM_COP_NUMB") //Сумма цифрами  в копейках
            field("CONTRAGENT_ADDRESS", get<CompanyInformation>().mainInfo.location) //Юридический адрес ИЗ ИНН
            field("CONTRAGENT_COR_WALLET", get<InformationBank>().mainInfo.correspondentAccount) //Корреспондентский счёт
            field("INN", get<CompanyInformation>().mainInfo.inn) //ИНН
            field("KPP", get<CompanyInformation>().mainInfo.kpp) //КПП ИЗ ИНН
            field("OGRN", get<CompanyInformation>().mainInfo.ogrn) //ОГРН ИЗ ИНН
            field("CONTRAGENT_WALLET", get<InformationBank>().settlementAccountNumber) //Расчётный счёт
            field("BIK", get<InformationBank>().mainInfo.bik) //БИК
            field("CONTRAGENT_FIO", get<CompanyInformation>().mainInfo.fullNameOfHolder) //ФИО ответсвенного от ООО
            field("CONTRAGENT_PROF", get<CompanyInformation>().mainInfo.post) //Должность ответственного от ООО
            field("CONTRAGENT_EMAIL", get<CompanyInformation>().email) //Адрес электронной почты ответственного от ООО
            field("CONTRAGENT_PHONE", get<CompanyInformation>().phone) //Номер телефона ответственного от ООО
            field("GENERAL_MANAGER_INIC", get<CompanyInformation>().mainInfo.initialsAfterSurname) //Инициалы и фамилия генерального директора
        }
    }
}

private fun DocumentBuilder.commonFields() {
    field("FULL_NAME", get<RussianFullName>().full())
    field("WITH_INITIALS", get<RussianFullName>().withInitials())
}
