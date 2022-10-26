import domain.datatypes.RussianFullName
import domain.documents.documentSet
import domain.documents.get

val documentSet = documentSet {
    document("/Документ1.docx") {
        field("\$WITH_INITIALS", get<RussianFullName>().withInitials())
    }
    if (get<RussianFullName>().firstName.startsWith("А", ignoreCase = true)) {
        document("/Только для тех, чьё имя начинается с А.docx") {
            field("\$FIRST_NAME", get<RussianFullName>().firstName)
        }
    }
}
