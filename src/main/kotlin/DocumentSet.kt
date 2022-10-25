import domain.datatypes.FullName
import domain.documents.documentSet
import domain.documents.get

val documentSet = documentSet {
    document("Шаблон.docx") {
        field("LAST_NAME", get<FullName.Russian>().lastName)
    }
    if (get<FullName.Russian>().firstName.startsWith("В")) {
        document("VV.docx") {
            field("FIRST_NAME", get<FullName.Russian>().firstName)
        }
        document("hmm.docx")
    }
}
