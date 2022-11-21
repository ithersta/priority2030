import com.ibm.icu.text.MessageFormat
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
        field("PP", get<PurchasePoint>().number.toString())
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
    var rubles=""
    var cops=""
    when (get<TermOfPayment>()){
        TermOfPayment.Prepaid->{
            val purchase=get<PurchaseCost>().costInRubles.number+"."+get<PurchaseCost>().costInCops.number
            val prepaidPayment=(purchase.toFloat()*0.3).toString()

            val rubl = prepaidPayment.substringBefore('.')
            val cop = prepaidPayment.substringAfter('.').substring(0,2)

            val ruPrescription = RuleBasedNumberFormat(
                Locale.forLanguageTag("ru"),
                RuleBasedNumberFormat.SPELLOUT
            )

            val rublesRu = ruPrescription.format(rubl)
            val copsRu = ruPrescription.format(cop)

            val rubleFormat = MessageFormat("{0, spellout} {0, plural, " +
                    "one {рубль}" +
                    "few {рубля}" +
                    "other {рублей}}", Locale.forLanguageTag("ru"))

            val copFormat = MessageFormat("{0, spellout} {0, plural, " +
                    "one {копейка}" +
                    "few {копейки}" +
                    "other {копеек}}", Locale.forLanguageTag("ru"))


            costInRubles=rubles
            costInCops=cops
            costInRublesPrescription=rublesRu
            costInCopsPrescription=copsRu
            rubles=rubleFormat.format(rubles)
            cops=copFormat.format(cops)


        }
        TermOfPayment.Fact->{
            costInRubles=get<PurchaseCost>().costInRubles.number
            costInCops=get<PurchaseCost>().costInCops.number
            costInRublesPrescription=get<PurchaseCost>().costInRublesPrescription
            costInCopsPrescription=get<PurchaseCost>().costInCopsPrescription
            rubles=get<PurchaseCost>().rubles
            cops=get<PurchaseCost>().cops
        }
        TermOfPayment.Partially->{}
    }
    field("PURCHASE_RUB_NUMB", costInRubles)
    field("PURCHASE_COP_NUMB", costInCops)
    field("PURCHASE_RUB", costInRublesPrescription)
    field("PURCHASE_COP", costInCopsPrescription)
    field("RUB", rubles)
    field("COP", cops)
}
private fun DocumentBuilder.financiallyResponsiblePerson(){
    var fio=""
    var contactNumber=""
    if (get<PurchaseDescription>().materialValuesAreNeeded){
        fio=get<FinanciallyResponsiblePerson>().fio.fio
        contactNumber=get<FinanciallyResponsiblePerson>().contactPhoneNumber.number
    }
    field("RESPONSIBLE_MEMBER_fio", fio)
    field("RESP_PRIVATE_PHONE", contactNumber)
}

private fun DocumentBuilder.responsibleForDocumentsPerson(){
    field("DOCUMENT_fio", get<ResponsibleForDocumentsPerson>().fio.fio)
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
    field("INICIATOR_fio", get<PurchaseIniciator>().fio.fio)
}

