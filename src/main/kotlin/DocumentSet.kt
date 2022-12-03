import domain.datatypes.*
import domain.documents.DocumentBuilder
import domain.documents.documentSet
import domain.documents.get
import extensions.*
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
    document("/documents/Служебная записка.docx") {
        purchaseObject()
        field("DESCRIPTION", get<PurchaseDescription>().shortJustification)
        field("LETTER", get<PurchaseDescription>().selectionLetter.letter)
        field("NUMBER", get<PurchaseDescription>().selectionIdentifier.indicator)
        field("REASON", get<PurchaseDescription>().fullJustification)
        field("PP", get<PurchasePoint>().number.point)
        iniciatorfio()
        purchaseCost()
    }
    document("/documents/Заявка на размещение.docx") {
        purchaseObject()
        field("CUSTOMER", get<PurchaseInitiatorDepartment>().department)
        termOfPaymentToStrings.get(get())?.let { field("PAYMENTWAY", it) }
        purchaseCost()

        financiallyResponsiblePerson()
        materialObjectNumber()

        responsibleForDocumentsPerson()
        field("EMAIL", get<ResponsibleForDocumentsPerson>().email.email)
        field("DEADLINE", get<PurchaseDeadlineAndDeliveryAddress>().deadline.format("dd.MM.uuuu"))
        field("PLACE", get<PurchaseDeadlineAndDeliveryAddress>().deliveryAddress)
        iniciatorfio()

    }
    document("/documents/Заявка на оплату.docx") {
        payment()
        iniciatorfio()

        financiallyResponsiblePerson()
        materialObjectNumber()
        responsibleForDocumentsPerson()
    }
}

private fun DocumentBuilder.ppAndPrice() {
    field("PP", get<PurchasePoint>().number.point)
    purchaseCost()
}

private fun DocumentBuilder.ipInfo() {
    field("ENPREPRENEURFIO", get<EntrepreneurInformation>().mainInfo.fullNameOfHolder)
    field("INICENPREPRENEUR", get<EntrepreneurInformation>().morphedFullName.initialsSurname)
    field("ENPREPRENEURINIC", get<EntrepreneurInformation>().morphedFullName.surnameInitials)
    field("OGRNIPNUMB", get<EntrepreneurInformation>().mainInfo.ogrn.value)
    field("OGRNIPDATE", get<EntrepreneurInformation>().mainInfo.orgrnData)
    field("ENTERPRENEURADDRESS", get<EntrepreneurInformation>().mainInfo.location)
    field("ENTERPRENEURINN", get<EntrepreneurInformation>().mainInfo.inn.value)
    field("ENTERPRENEUREMAIL", get<EntrepreneurInformation>().email.email)
    field("ENTERPRENEURPHONE", get<EntrepreneurInformation>().phone.number)
}

private fun DocumentBuilder.companyInformation() {
    field("GENERALMANAGERR", get<CompanyInformation>().morphedFullName.genitive)
    field("CONTRAGENTFULLNAME", get<CompanyInformation>().morphedFullName.original)
    field("CONTRAGENTSHORTNAME", get<CompanyInformation>().mainInfo.abbreviatedNameOfOrg)
    field("GENERALMANAGERINIC", get<CompanyInformation>().morphedFullName.initialsSurname)
    field("CONTRAGENTADDRESS", get<CompanyInformation>().mainInfo.location)
    field("INN", get<CompanyInformation>().mainInfo.inn.value)
    field("KPP", get<CompanyInformation>().mainInfo.kpp.value)
    field("OGRN", get<CompanyInformation>().mainInfo.ogrn.value)
    field("CONTRAGENTFIO", get<CompanyInformation>().morphedFullName.original)
    field("CONTRAGENTEMAIL", get<CompanyInformation>().email.email)
    field("CONTRAGENTPHONE", get<CompanyInformation>().phone.number)
}

private fun DocumentBuilder.bankInfo() {
    field("BIK", get<PaymentInformation>().bank.bic.value)
    field("CORRESPONDENTACCOUNT", get<PaymentInformation>().bank.correspondentAccount.value)
    field("BANK", get<PaymentInformation>().bank.name)
    field("SETTLEMENTACCOUNT", get<PaymentInformation>().settlementAccount.value)
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
