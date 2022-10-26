import domain.datatypes.RussianFullName
import domain.documents.documentSet
import domain.documents.get

val documentSet = documentSet {
    document("Шаблон.docx") {
        field("LAST_NAME", get<RussianFullName>().lastName)
    }
    if (get<RussianFullName>().firstName.startsWith("В")) {
        document("VV.docx") {
            field("FIRST_NAME", get<RussianFullName>().firstName)
        }
        document("hmm.docx")
    }
}
