import domain.datatypes.CompanyInformation
import domain.datatypes.EntrepreneurInformation
import domain.datatypes.InformationBank
import domain.datatypes.OrganizationType
import domain.documents.DocumentBuilder
import domain.documents.documentSet
import domain.documents.get
import ru.morpher.ws3.ClientBuilder

val clientMorpher = ClientBuilder().useToken(System.getenv("MORPHER_TOKEN")).build()
val documentSet = documentSet {
    when (get<OrganizationType>()) {
        OrganizationType.IP -> document("/Шаблон договора дл ИП С МЕТКАМИ.docx") {
            ipInfo()
            bankInfo()
            ppAndPrice()
        }

        OrganizationType.Ooo -> document("/Шаблон договора для ООО С МЕТКАМИ.docx") {
            CompanyInformation()
            bankInfo()
            ppAndPrice()
        }
    }
}

private fun DocumentBuilder.ppAndPrice() {
//  field("PP")
//  field("PURCHASE_RUB_NUMB")
//  field("PURCHASE_RUB")
//  field("SUMM_COP_NUMB")
}

private fun DocumentBuilder.ipInfo() {
    field("ENPREPRENEUR_FIO", get<EntrepreneurInformation>().mainInfo.fullNameOfHolder)
    field("ENPREPRENEUR_INIC_F", get<EntrepreneurInformation>().mainInfo.initialsAfterSurname)
    field("ENPREPRENEUR_INIC", get<EntrepreneurInformation>().mainInfo.surnameAfterInitials)
    field("OGRNIP_NUMB", get<EntrepreneurInformation>().mainInfo.ogrn)
    field("OGRNIP_DATE", get<EntrepreneurInformation>().mainInfo.orgrnData)
    field("CONTRAGENT_ADDRESS", get<EntrepreneurInformation>().mainInfo.location)
    field("ENTERPRENEUR_INN", get<EntrepreneurInformation>().mainInfo.inn)
    field("ENTERPRENEUR_EMAIL", get<EntrepreneurInformation>().email)
    field("ENTERPRENEUR_PHONE", get<EntrepreneurInformation>().phone)
}

private fun DocumentBuilder.CompanyInformation() {
    val fullNameOfHolder = get<CompanyInformation>().mainInfo.fullNameOfHolder
    print(clientMorpher.queriesLeftForToday())
    clientMorpher.russian().declension(fullNameOfHolder).run { field("GENERAL_MANAGER_R", genitive) }
    field("CONTRAGENT_FULL_NAME", fullNameOfHolder)
    field("CONTRAGENT_SHORT_NAME", get<CompanyInformation>().mainInfo.abbreviatedNameOfOrg)
    field("GENERAL_MANAGER_INIC", get<CompanyInformation>().mainInfo.initialsAfterSurname)
    field("GENERAL_MANAGER", get<CompanyInformation>().mainInfo.fullNameOfHolder)
    field("CONTRAGENT_ADDRESS", get<CompanyInformation>().mainInfo.location)
    field("INN", get<CompanyInformation>().mainInfo.inn)
    field("KPP", get<CompanyInformation>().mainInfo.kpp)
    field("OGRN", get<CompanyInformation>().mainInfo.ogrn)
    field("CONTRAGENT_FIO", get<CompanyInformation>().mainInfo.fullNameOfHolder)
    field("CONTRAGENT_PROF", get<CompanyInformation>().mainInfo.post)
    field("CONTRAGENT_EMAIL", get<CompanyInformation>().email)
    field("CONTRAGENT_PHONE", get<CompanyInformation>().phone)
}

private fun DocumentBuilder.bankInfo() {
    field("BIK", get<InformationBank>().mainInfo.bik)
    field("CONTRAGENT_COR_WALLET", get<InformationBank>().mainInfo.correspondentAccount)
    field("ENTERPRENEUR_BANK", get<InformationBank>().mainInfo.bankName)
    field("CONTRAGENT_WALLET", get<InformationBank>().settlementAccountNumber)
}
