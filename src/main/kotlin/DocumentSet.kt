import domain.datatypes.BankInfo
import domain.datatypes.OrganizationType
import domain.datatypes.RussianFullName
import domain.documents.DocumentBuilder
import domain.documents.documentSet
import domain.documents.get

val documentSet = documentSet {
    document("/Общий.docx") {
        field("FIRST_NAME", get<RussianFullName>().firstName)
        field("BIK", get<BankInfo>().bik)
        field("CORRESPONDENT_ACCOUNT", get<BankInfo>().correspondentAccount)
        field("BANK_NAME", get<BankInfo>().bankName)
        field("ACCOUNT_NUMBER", get<BankInfo>().settlementAccountNumber)
// todo: сделать так же как сверху!
//        field("FIRST_NAME", get<RussianFullName>().firstName)

    }
    when (get<OrganizationType>()) {
        OrganizationType.IP -> document("/Для ИП.docx") {
            commonFields()
            field("LAST_NAME", get<RussianFullName>().lastName)
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
