import domain.datatypes.*
import domain.documents.DocumentBuilder
import domain.documents.documentSet
import domain.documents.get
import telegram.resources.strings.CollectorStrings

private val termOfPaymentToStrings:Map<TermOfPayment,String> = mapOf(
    TermOfPayment.prepaid to CollectorStrings.TermOfPayment.Prepaid,
    TermOfPayment.fact to CollectorStrings.TermOfPayment.Fact,
    TermOfPayment.partially to CollectorStrings.TermOfPayment.Partially
)

val documentSet = documentSet {
    document("/documents/Служебная записка.docx"){
        purchaseObject()
        field("SHORT_DESCRIPTION", get<PurchaseDescription>().shortJustification)
        field("SEL_LETTER", get<PurchaseDescription>().selectionLetter)
        field("SEL_NUMB", get<PurchaseDescription>().selectionIdentifier)
        field("PURCHASE_REASON", get<PurchaseDescription>().fullJustification)
        field("PP", get<PurchasePoint>().number)
        iniciatorFIO()
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
        field("EM",get<ResponsibleForDocumentsPerson>().email)
        field("DEADLINE",get<PurchaseDeadlineAndDeliveryAddress>().deadline)
        field("PLACE",get<PurchaseDeadlineAndDeliveryAddress>().deliveryAddress)
        iniciatorFIO()

    }
    document("/documents/Заявка на оплату.docx"){
        purchaseCost()
        iniciatorFIO()

        financiallyResponsiblePerson()
        materialObjectNumber()
        responsibleForDocumentsPerson()
    }
}

private fun DocumentBuilder.purchaseCost(){
    field("PURCHASE_RUB_NUMB", get<PurchaseCost>().costInRubles)
    field("PURCHASE_COP_NUMB", get<PurchaseCost>().costInCops)
    field("PURCHASE_RUB", get<PurchaseCost>().costInRublesPrescription)
    field("PURCHASE_COP", get<PurchaseCost>().costInCopsPrescription)
    field("RUB", get<PurchaseCost>().rubles)
    field("COP", get<PurchaseCost>().cops)
}

private fun DocumentBuilder.financiallyResponsiblePerson(){
    var FIO=""
    var contactNumber=""
    if (get<PurchaseDescription>().materialValuesAreNeeded){
        FIO=get<FinanciallyResponsiblePerson>().FIO
        contactNumber=get<FinanciallyResponsiblePerson>().contactPhoneNumber
    }
    field("RESPONSIBLE_MEMBER_FIO", FIO)
    field("RESP_PRIVATE_PHONE", contactNumber)
}

private fun DocumentBuilder.responsibleForDocumentsPerson(){
    field("DOCUMENT_FIO", get<ResponsibleForDocumentsPerson>().FIO)
    field("DOC_PRIVATE_PHONE", get<ResponsibleForDocumentsPerson>().contactPhoneNumber)
}

private fun DocumentBuilder.materialObjectNumber(){
    var number=""
    if(get<PurchaseDescription>().materialValuesAreNeeded){
        number=get<MaterialObjectNumber>().number
    }
    field("RESP_POINT", number)
}

private fun DocumentBuilder.purchaseObject(){
    field("PURCHASE_NAME", get<PurchaseObject>().shortName)
}

private fun DocumentBuilder.iniciatorFIO(){
    field("INICIATOR_FIO", get<PurchaseIniciator>().FIO)
}

