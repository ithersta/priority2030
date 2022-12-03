import domain.datatypes.*
import domain.documents.DocumentBuilder
import domain.documents.documentSet
import domain.documents.get
import extensions.copecksUnit
import extensions.rublesUnit
import extensions.spelloutCopecks
import extensions.spelloutRubles
import telegram.resources.strings.CollectorStrings
import java.math.BigDecimal

private val termOfPaymentToStrings: Map<TermOfPayment, String> = mapOf(
    TermOfPayment.Prepaid to CollectorStrings.TermOfPayment.Prepaid,
    TermOfPayment.Fact to CollectorStrings.TermOfPayment.Fact,
    TermOfPayment.Partially to CollectorStrings.TermOfPayment.Partially
)

val documentSet = documentSet {
    when (get<OrganizationType>()) {
        OrganizationType.IP -> document("/documents/Договор для ИП.docx") {
            ipInfo()
            bankInfo()
            ppAndPrice()
        }

        OrganizationType.Ooo -> document("/documents/Договор для ООО.docx") {
            companyInformation()
            bankInfo()
            ppAndPrice()
        }
    }
}

private fun DocumentBuilder.ppAndPrice() {
    field("PP", get<PurchasePoint>().number.point)
    purchaseCost()
}

private fun DocumentBuilder.ipInfo() {
    field("ENPREPRENEURFIO", get<EntrepreneurInformation>().mainInfo.fullNameOfHolder)
    field("INICENPREPRENEUR", get<EntrepreneurInformation>().mainInfo.initialsAfterSurname)
    field("ENPREPRENEURINIC", get<EntrepreneurInformation>().mainInfo.surnameAfterInitials)
    field("OGRNIPNUMB", get<EntrepreneurInformation>().mainInfo.ogrn)
    field("OGRNIPDATE", get<EntrepreneurInformation>().mainInfo.orgrnData)
    field("ENTERPRENEURADDRESS", get<EntrepreneurInformation>().mainInfo.location)
    field("ENTERPRENEURINN", get<EntrepreneurInformation>().mainInfo.inn.value)
    field("ENTERPRENEUREMAIL", get<EntrepreneurInformation>().email)
    field("ENTERPRENEURPHONE", get<EntrepreneurInformation>().phone)
}

private fun DocumentBuilder.companyInformation() {
    val fullNameOfHolder = get<CompanyInformation>().mainInfo.fullNameOfHolder
    field("GENERALMANAGERR", fullNameOfHolder.genitive)
    field("CONTRAGENTFULLNAME", fullNameOfHolder.original)
    field("CONTRAGENTSHORTNAME", get<CompanyInformation>().mainInfo.abbreviatedNameOfOrg)
    field("GENERALMANAGERINIC", fullNameOfHolder.initialsSurname)
    field("CONTRAGENTADDRESS", get<CompanyInformation>().mainInfo.location)
    field("INN", get<CompanyInformation>().mainInfo.inn.value)
    field("KPP", get<CompanyInformation>().mainInfo.kpp.value)
    field("OGRN", get<CompanyInformation>().mainInfo.ogrn)
    field("CONTRAGENTFIO", fullNameOfHolder.original)
    field("CONTRAGENTEMAIL", get<CompanyInformation>().email.email)
    field("CONTRAGENTPHONE", get<CompanyInformation>().phone.number)
}

private fun DocumentBuilder.bankInfo() {
    field("BIK", get<InformationBank>().bank.bic.value)
    field("CORRESPONDENTACCOUNT", get<InformationBank>().bank.correspondentAccount.value)
    field("BANK", get<InformationBank>().bank.name)
    field("SETTLEMENTACCOUNT", get<InformationBank>().settlementAccount.value)
}

private fun DocumentBuilder.purchaseCost() = get<PurchaseCost>().run {
    field("RUBLENUMB", rubles.toString())
    field("COPEEKNUMB", "%02d".format(copecks))
    field("RUBLES", spelloutRubles())
    field("COPEEKS", spelloutCopecks())
    field("RUBS", rublesUnit())
    field("COPS", copecksUnit())
}

private fun DocumentBuilder.payment() {
    val payment = when (get<TermOfPayment>()) {
        TermOfPayment.Prepaid -> get<PurchaseCost>() * BigDecimal("0.3")
        TermOfPayment.Fact -> get()
        TermOfPayment.Partially -> null
    }
    field("RUBLENUMB", payment?.rubles?.toString().orEmpty())
    field("COPEEKNUMB", payment?.copecks?.let { "%02d".format(it) }.orEmpty())
    field("RUBLES", payment?.spelloutRubles().orEmpty())
    field("COPEEKS", payment?.spelloutCopecks().orEmpty())
}


private fun DocumentBuilder.financiallyResponsiblePerson() {
    val person = if (get<PurchaseDescription>().materialValuesAreNeeded) {
        get<FinanciallyResponsiblePerson>()
    } else {
        null
    }
    field("RESPONSIBLEMEMBERFIO", person?.fio?.fio.orEmpty())
    field("RESPPRIVATEPHONE", person?.contactPhoneNumber?.number.orEmpty())
}

private fun DocumentBuilder.responsibleForDocumentsPerson() {
    field("DOCUMENTFIO", get<ResponsibleForDocumentsPerson>().fio.fio)
    field("DOCPRIVATEPHONE", get<ResponsibleForDocumentsPerson>().contactPhoneNumber.number)
}

private fun DocumentBuilder.materialObjectNumber() {
    val number = if (get<PurchaseDescription>().materialValuesAreNeeded) {
        get<MaterialObjectNumber>().number
    } else {
        null
    }
    field("RESPPOINT", number?.toString().orEmpty())
}

private fun DocumentBuilder.purchaseObject() {
    field("NAME", get<PurchaseObject>().shortName)
}

private fun DocumentBuilder.iniciatorfio() {
    field("INICIATORFIO", get<PurchaseIniciator>().fio.fio)
}
