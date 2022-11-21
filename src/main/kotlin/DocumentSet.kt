import com.ibm.icu.text.RuleBasedNumberFormat
import domain.datatypes.*
import domain.documents.DocumentBuilder
import domain.documents.documentSet
import domain.documents.get
import telegram.resources.strings.CollectorStrings
import java.util.*

private val termOfPaymentToStrings:Map<TermOfPayment,String> = mapOf(
    TermOfPayment.Prepaid to CollectorStrings.TermOfPayment.Prepaid,
    TermOfPayment.Fact to CollectorStrings.TermOfPayment.Fact,
    TermOfPayment.Partially to CollectorStrings.TermOfPayment.Partially
)

val documentSet = documentSet {
    document("/documents/Служебная записка.docx"){
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
        field("DEADLINE",get<PurchaseDeadlineAndDeliveryAddress>().deadline.date)
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

private fun DocumentBuilder.purchaseCost(){
    field("PURCHASE_RUB_NUMB", get<PurchaseCost>().costInRubles.number)
    field("PURCHASE_COP_NUMB", get<PurchaseCost>().costInCops.number)
    field("PURCHASE_RUB", get<PurchaseCost>().costInRublesPrescription)
    field("PURCHASE_COP", get<PurchaseCost>().costInCopsPrescription)
    field("RUB", get<PurchaseCost>().rubles)
    field("COP", get<PurchaseCost>().cops)
}

private fun DocumentBuilder.payment(){
    var costInRubles =""
    var costInCops=""
    var costInRublesPrescription=""
    var costInCopsPrescription=""

    when (get<TermOfPayment>()){
        TermOfPayment.Prepaid->{
            val purchase=get<PurchaseCost>().costInRubles.number+"."+get<PurchaseCost>().costInCops.number
            val prepaidPayment=(purchase.toFloat()*0.3).toString()

            val rubl = prepaidPayment.substringBefore('.')
            val cop = prepaidPayment.substringAfter('.').take(2)

            val ruPrescription = RuleBasedNumberFormat(
                Locale.forLanguageTag("ru"),
                RuleBasedNumberFormat.SPELLOUT
            )

            val rublesRu = ruPrescription.format(rubl.toInt())
            val copsRu = ruPrescription.format(cop.toInt())

            costInRubles=rubl
            costInCops=cop
            costInRublesPrescription=rublesRu
            costInCopsPrescription=copsRu
        }
        TermOfPayment.Fact->{
            costInRubles=get<PurchaseCost>().costInRubles.number
            costInCops=get<PurchaseCost>().costInCops.number
            costInRublesPrescription=get<PurchaseCost>().costInRublesPrescription
            costInCopsPrescription=get<PurchaseCost>().costInCopsPrescription
        }
        TermOfPayment.Partially->{}
    }
    field("PURCHASE_RUB_NUMB", costInRubles)
    field("PURCHASE_COP_NUMB", costInCops)
    field("PURCHASE_RUB", costInRublesPrescription)
    field("PURCHASE_COP", costInCopsPrescription)
}
private fun DocumentBuilder.financiallyResponsiblePerson(){
    var fio=""
    var contactNumber=""
    if (get<PurchaseDescription>().materialValuesAreNeeded){
        fio=get<FinanciallyResponsiblePerson>().fio.fio
        contactNumber=get<FinanciallyResponsiblePerson>().contactPhoneNumber.number
    }
    field("RESPONSIBLE_MEMBER_FIO", fio)
    field("RESP_PRIVATE_PHONE", contactNumber)
}

private fun DocumentBuilder.responsibleForDocumentsPerson(){
    field("DOCUMENT_FIO", get<ResponsibleForDocumentsPerson>().fio.fio)
    field("DOC_PRIVATE_PHONE", get<ResponsibleForDocumentsPerson>().contactPhoneNumber.number)
}

private fun DocumentBuilder.materialObjectNumber(){
    var number=""
    if(get<PurchaseDescription>().materialValuesAreNeeded){
        number=get<MaterialObjectNumber>().number.number
    }
    field("RESP_POINT", number)
}

private fun DocumentBuilder.purchaseObject(){
    field("PURCHASE_NAME", get<PurchaseObject>().shortName)
}

private fun DocumentBuilder.iniciatorfio(){
    field("INICIATOR_FIO", get<PurchaseIniciator>().fio.fio)
}

