import domain.datatypes.*
import domain.documents.DocumentBuilder
import domain.documents.documentSet
import domain.documents.get
import extensions.copecksUnit
import extensions.format
import extensions.rublesUnit
import extensions.spelloutRubles
import org.koin.core.component.inject
import services.morpher.Morpher
import telegram.resources.strings.CollectorStrings
import java.math.BigDecimal
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private val termOfPaymentToStrings: Map<TermOfPayment, String> = mapOf(
    TermOfPayment.Prepaid to CollectorStrings.TermOfPayment.Prepaid,
    TermOfPayment.Fact to CollectorStrings.TermOfPayment.Fact,
    TermOfPayment.Partially to CollectorStrings.TermOfPayment.Partially
)

val documentSet = documentSet {
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
        paymentSum()
        iniciatorfio()

        financiallyResponsiblePerson()
        materialObjectNumber()
        responsibleForDocumentsPerson()
    }
    when (get<OrganizationType>()) {
        OrganizationType.IP -> document("/documents/Договор для ИП.docx") {
            ipInformation()
            paymentDetails()
            purchaseCost()
            field("PP", get<PurchasePoint>().number.point)
        }

        OrganizationType.Ooo -> document("/documents/Договор для ООО.docx") {
            companyInformation()
            paymentDetails()
            purchaseCost()
            field("PP", get<PurchasePoint>().number.point)
        }
    }
}

private fun DocumentBuilder.ipInformation() = get<EntrepreneurInformation>().run {
    val morpher: Morpher by inject()
    val morphedFullName = morpher.morphFullName(mainInfo.fullNameOfHolder)
    val dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    field("ENPREPRENEURFIO", mainInfo.fullNameOfHolder)
    field("INICENPREPRENEUR", morphedFullName.initialsSurname)
    field("ENPREPRENEURINIC", morphedFullName.surnameInitials)
    field("OGRNIPNUMB", mainInfo.ogrn.value)
    field("OGRNIPDATE", mainInfo.ogrnDate.format(dateTimeFormatter))
    field("ENTERPRENEURADDRESS", mainInfo.location)
    field("ENTERPRENEURINN", mainInfo.inn.value)
    field("ENTERPRENEUREMAIL", email.email)
    field("ENTERPRENEURPHONE", phone.number)
}

private fun DocumentBuilder.companyInformation() = get<CompanyInformation>().run {
    val morpher: Morpher by inject()
    val morphedFullName = morpher.morphFullName(mainInfo.fullNameOfHolder)
    field("GENERALMANAGERR", morphedFullName.genitive)
    field("CONTRAGENTFULLNAME", mainInfo.fullName)
    field("CONTRAGENTSHORTNAME", mainInfo.shortName)
    field("GENERALMANAGERINIC", morphedFullName.initialsSurname)
    field("CONTRAGENTADDRESS", mainInfo.location)
    field("INN", mainInfo.inn.value)
    field("KPP", mainInfo.kpp.value)
    field("OGRN", mainInfo.ogrn.value)
    field("CONTRAGENTFIO", morphedFullName.original)
    field("CONTRAGENTEMAIL", email.email)
    field("CONTRAGENTPHONE", phone.number)
}

private fun DocumentBuilder.paymentDetails() {
    field("BIK", get<PaymentDetails>().bank.bik.value)
    field("CORRESPONDENTACCOUNT", get<PaymentDetails>().bank.correspondentAccount.value)
    field("BANK", get<PaymentDetails>().bank.name)
    field("SETTLEMENTACCOUNT", get<PaymentDetails>().settlementAccount.value)
}

private fun DocumentBuilder.purchaseCost() = get<PurchaseCost>().run {
    field("RUBLENUMB", rubles.toString())
    field("COPEEKNUMB", "%02d".format(copecks))
    field("RUBLES", spelloutRubles())
    field("RUBS", rublesUnit())
    field("COPS", copecksUnit())
}

private fun DocumentBuilder.paymentSum() {
    val payment = when (get<TermOfPayment>()) {
        TermOfPayment.Prepaid -> get<PurchaseCost>() * BigDecimal("0.3")
        TermOfPayment.Fact -> get()
        TermOfPayment.Partially -> null
    }
    field("RUBLENUMB", payment?.rubles?.toString().orEmpty())
    field("COPEEKNUMB", payment?.copecks?.let { "%02d".format(it) }.orEmpty())
    field("RUBLES", payment?.spelloutRubles().orEmpty())
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
