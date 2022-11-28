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
    document("/documents/Служебная записка.docx") {
        purchaseObject()
        field("SHORT_DESCRIPTION", get<PurchaseDescription>().shortJustification)
        field("SEL_LETTER", get<PurchaseDescription>().selectionLetter.letter)
        field("SEL_NUMB", get<PurchaseDescription>().selectionIdentifier.indicator)
        field("PURCHASE_REASON", get<PurchaseDescription>().fullJustification)
        field("PP", get<PurchasePoint>().number.point)
        iniciatorfio()
        purchaseCost()
    }
    document("/documents/Заявка на размещение.docx"){
        purchaseObject()
        field("CUSTOMER", get<PurchaseInitiatorDepartment>().department)
        termOfPaymentToStrings.get(get())?.let { field("PAYMENT_WAY", it) }
        purchaseCost()

        financiallyResponsiblePerson()
        materialObjectNumber()

        responsibleForDocumentsPerson()
        field("EM",get<ResponsibleForDocumentsPerson>().email.email)
        field("DEADLINE",get<PurchaseDeadlineAndDeliveryAddress>().deadline.format("dd.MM.uuuu"))
        field("PLACE",get<PurchaseDeadlineAndDeliveryAddress>().deliveryAddress)
        iniciatorfio()

    }
    document("/documents/Заявка на оплату.docx"){
        payment()
        iniciatorfio()

        financiallyResponsiblePerson()
        materialObjectNumber()
        responsibleForDocumentsPerson()
    }
}

private fun DocumentBuilder.purchaseCost() = get<PurchaseCost>().run {
    field("RUBLESNUMB", rubles.toString())
    field("COPEEKSNUMB", "%02d".format(copecks))
    field("RUBLES", spelloutRubles())
    field("COPEEKS", spelloutCopecks())
    field("RUB", rublesUnit())
    field("COP", copecksUnit())
}

private fun DocumentBuilder.payment() {
    val payment = when (get<TermOfPayment>()) {
        TermOfPayment.Prepaid -> get<PurchaseCost>() * BigDecimal("0.3")
        TermOfPayment.Fact -> get()
        TermOfPayment.Partially -> null
    }
    field("RUBLESNUMB", payment?.rubles?.toString().orEmpty())
    field("COPEEKSNUMB", payment?.copecks?.let { "%02d".format(it) }.orEmpty())
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

private fun DocumentBuilder.responsibleForDocumentsPerson(){
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

private fun DocumentBuilder.purchaseObject(){
    field("NAME", get<PurchaseObject>().shortName)
}

private fun DocumentBuilder.iniciatorfio(){
    field("INICIATORFIO", get<PurchaseIniciator>().fio.fio)
}

