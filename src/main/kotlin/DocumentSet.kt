import domain.datatypes.FullName
import domain.documents.documentSet
import domain.documents.get

val documentSet = documentSet {
    document("Шаблон.docx") {
        field("LAST_NAME", get<FullName>().lastName)
    }
    if (get<FullName>().firstName.startsWith("В")) {
        document("VV.docx") {
            field("FIRST_NAME", get<FullName>().firstName)
        }
        document("hmm.docx")
    }
}
