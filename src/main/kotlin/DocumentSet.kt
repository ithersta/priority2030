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
        field("RESP_PHONE",get<FinanciallyResponsiblePerson>().workPhoneNumber)
        materialObjectNumber()
        responsibleForDocumentsPerson()
        field("DOC_PHONE",get<ResponsibleForDocumentsPerson>().workPhoneNumber)
        field("EM",get<ResponsibleForDocumentsPerson>().email)
        field("DEADLINE",get<PurchaseDeadlineAndDeliveryAddress>().deadline)
        field("PLACE",get<PurchaseDeadlineAndDeliveryAddress>().deliveryAddress)
        iniciatorFIO()
    }
    document("/documents/Заявка на оплату.docx"){
        purchaseCost()
        iniciatorFIO()
        financiallyResponsiblePerson()
        responsibleForDocumentsPerson()
        materialObjectNumber()
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
    field("RESPONSIBLE_MEMBER_FIO", get<FinanciallyResponsiblePerson>().FIO)
    field("RESP_PRIVATE_PHONE", get<FinanciallyResponsiblePerson>().contactPhoneNumber)
}

private fun DocumentBuilder.responsibleForDocumentsPerson(){
    field("DOCUMENT_FIO", get<ResponsibleForDocumentsPerson>().FIO)
    field("DOC_PRIVATE_PHONE", get<ResponsibleForDocumentsPerson>().contactPhoneNumber)
}

private fun DocumentBuilder.materialObjectNumber(){
    field("RESP_POINT", get<MaterialObjectNumber>().number)
}

private fun DocumentBuilder.purchaseObject(){
    field("PURCHASE_NAME", get<PurchaseObject>().shortName)
}

private fun DocumentBuilder.iniciatorFIO(){
    field("INICIATOR_FIO", get<PurchaseIniciator>().FIO)
}

