import domain.datatypes.*
import domain.documents.DocumentSet
import domain.entities.*
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import services.BikParser
import services.SbisParser
import telegram.Docx
import java.io.File
import kotlin.test.fail

internal class DocumentSetTest {

    private fun Collection<FieldData>.toFieldDataMap() = associateBy { it::class }

    @Test
    fun `Для ООО`() {
        startKoin { modules(priority2030Module) }
        val dataset = listOf(
            OrganizationType.Ooo,
            CompanyInformation(
                SbisParser().getOrgInfo(OooInn.of("7733345489")!!, Kpp.of("773301001")!!)!!,
                PhoneNumber.of("+79000000000")!!,
                Email.of("s@g.com")!!
            ),
            PurchaseObject("Короткое имя"),
            PurchaseDescription(
                "Краткое обоснование",
                SelectionLetter.of("а")!!,
                SelectionIdentifier.of("ПРГ1")!!,
                "Полное обоснование",
                false
            ),
            PurchasePoint(PurchasePoints.of("2")!!),
            PurchaseIniciator(Fio.of("Г.Г. Дударь")!!),
            PurchaseCost(1300),
            TermOfPayment.Prepaid,
            PurchaseInitiatorDepartment("Название отдела"),
            ResponsibleForDocumentsPerson(
                Fio.of("Ж.Ж. Ответственный")!!,
                PhoneNumber.of("+79000000000")!!,
                Email.of("responsible@gmail.com")!!
            ),
            PurchaseDeadlineAndDeliveryAddress(LocalDate.fromEpochDays(400), "Адрес"),
            PaymentInformation(
                BikParser().parseWebPage(Bik.of("044525225")!!)!!,
                SettlementAccount.of("30301810000006000001")!!
            )
        )
        when (val result = documentSet.build(dataset.toFieldDataMap())) {
            is DocumentSet.Result.MissingData -> fail(result.kClass.simpleName)
            is DocumentSet.Result.OK -> {
                result.documents.forEach {
                    File("build/tmp/test/" + it.filename).writeBytes(Docx.load(it))
                }
            }
        }
    }
}
