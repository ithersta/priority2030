import domain.datatypes.BankInfo
import domain.datatypes.IpInfo
import domain.datatypes.OrganizationType
import domain.datatypes.RussianFullName
import domain.documents.DocumentBuilder
import domain.documents.documentSet
import domain.documents.get
import telegram.entities.state.IpCollectorState

val documentSet = documentSet {
    document("/Общий.docx") {
        field("FIRST_NAME", get<RussianFullName>().firstName)
        field("BIK", get<BankInfo>().bik)
        field("CORRESPONDENT_ACCOUNT", get<BankInfo>().correspondentAccount)
        field("BANK_NAME", get<BankInfo>().bankName)
        field("ACCOUNT_NUMBER", get<BankInfo>().settlementAccountNumber)
// TODO: сделать так же как сверху!
//        field("FIRST_NAME", get<RussianFullName>().firstName)

    }
    when (get<OrganizationType>()) {
        OrganizationType.IP -> document("/Для ИП.docx") {
            commonFields()
            field("ENPREPRENEUR_FIO",get<IpInfo>().fullNameIp) // ФИО предпринимателя ИЗ ОГРНИП
            field()
            field()
            field()
            field()
//            field("LAST_NAME", get<RussianFullName>().lastName)
//            field("INN", get<IpInfo>().inn)

        }

        OrganizationType.Ooo -> document("/Для ООО.docx") {
            commonFields()

        }
    }
}

private fun DocumentBuilder.commonFields() {
    field("FULL_NAME", get<RussianFullName>().full())
    field("WITH_INITIALS", get<RussianFullName>().withInitials())
}
