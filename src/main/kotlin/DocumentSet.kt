import domain.datatypes.*
import domain.documents.DocumentBuilder
import domain.documents.documentSet
import domain.documents.get
import ru.morpher.ws3.ClientBuilder

//private val termOfPaymentToStrings: Map<TermOfPayment, String> = mapOf(
//    TermOfPayment.Prepaid to CollectorStrings.TermOfPayment.Prepaid,
//    TermOfPayment.Fact to CollectorStrings.TermOfPayment.Fact,
//    TermOfPayment.Partially to CollectorStrings.TermOfPayment.Partially
//)

val documentSet = documentSet {
    when (get<OrganizationType>()) {
        OrganizationType.IP -> document("/Шаблон договора дл ИП С МЕТКАМИ.docx") {
            ipInfo()
            bankInfo()
            ppAndPrice()
        }

        OrganizationType.Ooo -> document("/Шаблон договора для ООО С МЕТКАМИ.docx") {
            companyInformation()
            bankInfo()
            ppAndPrice()
        }
    }
}

private fun DocumentBuilder.ppAndPrice() {
    field("PP", get<PurchasePoint>().number.point)
    field("PRICE_NUM", get<InformationCost>().price.toString())
    field("PRICE_WORD", get<InformationCost>().inWords())
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

private fun DocumentBuilder.companyInformation() {
    val clientMorpher = ClientBuilder().useToken(System.getenv("MORPHER_TOKEN")).build()
    val fullNameOfHolder = get<CompanyInformation>().mainInfo.fullNameOfHolder
    println(clientMorpher.queriesLeftForToday())
    if (clientMorpher.queriesLeftForToday() != 0) {
        field("GENERAL_MANAGER_R",  clientMorpher.russian().declension(fullNameOfHolder).genitive)
        println(clientMorpher.queriesLeftForToday())
    } else {
        //  todo: !
    }
    field("CONTRAGENT_FULL_NAME", fullNameOfHolder)
    field("CONTRAGENT_SHORT_NAME", get<CompanyInformation>().mainInfo.abbreviatedNameOfOrg)
    field("GENERAL_MANAGER_INIC", get<CompanyInformation>().mainInfo.initialsAfterSurname)
    field("GENERAL_MANAGER", get<CompanyInformation>().mainInfo.fullNameOfHolder)
    field("CONTRAGENT_ADDRESS", get<CompanyInformation>().mainInfo.location)
    field("INN", get<CompanyInformation>().mainInfo.inn)
    field("KPP", get<CompanyInformation>().mainInfo.kpp)
    field("OGRN", get<CompanyInformation>().mainInfo.ogrn)
    field("CONTRAGENT_FIO", get<CompanyInformation>().mainInfo.fullNameOfHolder)
    field("CONTRAGENT_EMAIL", get<CompanyInformation>().email)
    field("CONTRAGENT_PHONE", get<CompanyInformation>().phone)
}

private fun DocumentBuilder.bankInfo() {
    field("BIK", get<InformationBank>().mainInfo.bik)
    field("COR_WALLET", get<InformationBank>().mainInfo.correspondentAccount)
    field("BANK", get<InformationBank>().mainInfo.bankName)
    field("WALLET", get<InformationBank>().settlementAccountNumber)
}

//private fun DocumentBuilder.purchaseCost() = get<PurchaseCost>().run {
//    field("RUBLENUMB", rubles.toString())
//    field("COPEEKNUMB", "%02d".format(copecks))
//    field("RUBLES", spelloutRubles())
//    field("COPEEKS", spelloutCopecks())
//    field("RUBS", rublesUnit())
//    field("COPS", copecksUnit())
//}
//
//private fun DocumentBuilder.payment() {
//    val payment = when (get<TermOfPayment>()) {
//        TermOfPayment.Prepaid -> get<PurchaseCost>() * BigDecimal("0.3")
//        TermOfPayment.Fact -> get()
//        TermOfPayment.Partially -> null
//    }
//    field("RUBLENUMB", payment?.rubles?.toString().orEmpty())
//    field("COPEEKNUMB", payment?.copecks?.let { "%02d".format(it) }.orEmpty())
//    field("RUBLES", payment?.spelloutRubles().orEmpty())
//    field("COPEEKS", payment?.spelloutCopecks().orEmpty())
//}
//
//
//private fun DocumentBuilder.financiallyResponsiblePerson() {
//    val person = if (get<PurchaseDescription>().materialValuesAreNeeded) {
//        get<FinanciallyResponsiblePerson>()
//    } else {
//        null
//    }
//    field("RESPONSIBLEMEMBERFIO", person?.fio?.fio.orEmpty())
//    field("RESPPRIVATEPHONE", person?.contactPhoneNumber?.number.orEmpty())
//}
//
//private fun DocumentBuilder.responsibleForDocumentsPerson() {
//    field("DOCUMENTFIO", get<ResponsibleForDocumentsPerson>().fio.fio)
//    field("DOCPRIVATEPHONE", get<ResponsibleForDocumentsPerson>().contactPhoneNumber.number)
//}
//
//private fun DocumentBuilder.materialObjectNumber() {
//    val number = if (get<PurchaseDescription>().materialValuesAreNeeded) {
//        get<MaterialObjectNumber>().number
//    } else {
//        null
//    }
//    field("RESPPOINT", number?.toString().orEmpty())
//}
//
//private fun DocumentBuilder.purchaseObject() {
//    field("NAME", get<PurchaseObject>().shortName)
//}
//
//private fun DocumentBuilder.iniciatorfio() {
//    field("INICIATORFIO", get<PurchaseIniciator>().fio.fio)
//}