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
        field("DESCRIPTION", get<PurchaseDescription>().shortJustification)
        field("LETTER", get<PurchaseDescription>().selectionLetter.letter)
        field("NUMB", get<PurchaseDescription>().selectionIdentifier.indicator)
        field("REASON", get<PurchaseDescription>().fullJustification)
        field("PP", get<PurchasePoint>().number.point)
        iniciatorfio()
        purchaseCost()
    }
    document("/documents/Заявка на размещение.docx"){
        purchaseObject()
        field("CUSTOMER", get<PurchaseInitiatorDepartment>().department)
        termOfPaymentToStrings.get(get())?.let { field("PAYMENTWAY", it) }
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
    field("RUBLESNUMB", get<PurchaseCost>().costInRubles.number)
    field("COPEEKSNUMB", get<PurchaseCost>().costInCops.number)
    field("RUBLES", get<PurchaseCost>().costInRublesPrescription)
    field("COPEEKS", get<PurchaseCost>().costInCopsPrescription)
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
    field("RUBLESNUMB", costInRubles)
    field("COPEEKSNUMB", costInCops)
    field("RUBLES", costInRublesPrescription)
    field("COPEEKS", costInCopsPrescription)
}
private fun DocumentBuilder.financiallyResponsiblePerson(){
    var fio=""
    var contactNumber=""
    if (get<PurchaseDescription>().materialValuesAreNeeded){
        fio=get<FinanciallyResponsiblePerson>().fio.fio
        contactNumber=get<FinanciallyResponsiblePerson>().contactPhoneNumber.number
    }
    field("RESPONSIBLEMEMBERFIO", fio)
    field("RESPPRIVATEPHONE", contactNumber)
}

private fun DocumentBuilder.responsibleForDocumentsPerson(){
    field("DOCUMENTFIO", get<ResponsibleForDocumentsPerson>().fio.fio)
    field("DOCPRIVATEPHONE", get<ResponsibleForDocumentsPerson>().contactPhoneNumber.number)
}

private fun DocumentBuilder.materialObjectNumber(){
    var number=""
    if(get<PurchaseDescription>().materialValuesAreNeeded){
        number=get<MaterialObjectNumber>().number.number
    }
    field("RESPPOINT", number)
}

private fun DocumentBuilder.purchaseObject(){
    field("NAME", get<PurchaseObject>().shortName)
}

private fun DocumentBuilder.iniciatorfio(){
    field("INICIATORFIO", get<PurchaseIniciator>().fio.fio)
}

