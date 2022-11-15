import domain.datatypes.*
import domain.documents.DocumentBuilder
import domain.documents.documentSet
import domain.documents.get

val documentSet = documentSet {
//    document("/documents/Общий.docx") {
//        field("FIRST_NAME", get<RussianFullName>().firstName)
//    }
//    when (get<OrganizationType>()) {
//        OrganizationType.IP -> document("/documents/Для ИП.docx") {
//            commonFields()
//            field("LAST_NAME", get<RussianFullName>().lastName)
//        }
//        OrganizationType.Ooo -> document("/documents/Для ООО.docx") {
//            commonFields()
//        }
//    }
    document("/documents/Служебная записка.docx"){
        field("PURCHASE_NAME", get<PurchaseDescription>().shortName)
        field("SHORT_DESCRIPTION", get<PurchaseDescription>().shortJustification)
        field("SEL_LETTER", get<PurchaseDescription>().selectionLetter)
        field("SEL_NUMB", get<PurchaseDescription>().selectionIdentifier)
        field("PURCHASE_REASON", get<PurchaseDescription>().fullJustification)
        field("PP", get<PurchasePoint>().number)
        field("INICIATOR_FIO", get<PurchaseIniciator>().FIO)
        field("PURCHASE_RUB_NUMB", get<PurchaseCost>().costInRubles)
        field("SUMM_COP_NUMB", get<PurchaseCost>().costInCops)
        field("PURCHASE_RUB", get<PurchaseCost>().costInRublesPrescription)
        field("SUMM_COP", get<PurchaseCost>().costInCopsPrescription)
    }
}

private fun DocumentBuilder.commonFields() {
    field("FULL_NAME", get<RussianFullName>().full())
    field("WITH_INITIALS", get<RussianFullName>().withInitials())
}
