import domain.datatypes.*
import domain.documents.DocumentBuilder
import domain.documents.documentSet
import domain.documents.get

val documentSet = documentSet {
    document("/documents/Служебная записка.docx"){
        field("PURCHASE_NAME", get<PurchaseDescription>().shortName)
        field("SHORT_DESCRIPTION", get<PurchaseDescription>().shortJustification)
        field("SEL_LETTER", get<PurchaseDescription>().selectionLetter)
        field("SEL_NUMB", get<PurchaseDescription>().selectionIdentifier)
        field("PURCHASE_REASON", get<PurchaseDescription>().fullJustification)
        field("PP", get<PurchasePoint>().number)
        field("INICIATOR_FIO", get<PurchaseIniciator>().FIO)
        purchaseCost()
    }
    document("/documents/Заявка на оплату.docx"){
        purchaseCost()
        field("INICIATOR_FIO", get<PurchaseIniciator>().FIO)
        financiallyResponsiblePerson()
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
    field("RESPONSIBLE_MEMBER_FIO", get<FinanciallyResponsiblePerson>().FIO)
    field("RESP_PHONE", get<FinanciallyResponsiblePerson>().contactPhoneNumber)
}

private fun DocumentBuilder.responsibleForDocumentsPerson(){
    field("DOCUMENT_FIO", get<ResponsibleForDocumentsPerson>().FIO)
    field("DOC_PHONE", get<ResponsibleForDocumentsPerson>().contactPhoneNumber)
}
