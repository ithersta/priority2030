import domain.datatypes.OrganizationType
import domain.datatypes.RussianFullName
import domain.documents.DocumentBuilder
import domain.documents.documentSet
import domain.documents.get

val documentSet = documentSet {
    document("/documents/Общий.docx") {
        field("FIRST_NAME", get<RussianFullName>().firstName)
    }
    when (get<OrganizationType>()) {
        OrganizationType.IP -> document("/documents/Для ИП.docx") {
            commonFields()
            field("LAST_NAME", get<RussianFullName>().lastName)
        }
        OrganizationType.Ooo -> document("/documents/Для ООО.docx") {
            commonFields()
        }
    }
}

private fun DocumentBuilder.commonFields() {
    field("FULL_NAME", get<RussianFullName>().full())
    field("WITH_INITIALS", get<RussianFullName>().withInitials())
}
