package domain.documents

import org.koin.core.component.KoinComponent

@DocumentDsl
class DocumentSetContext(override val fieldDataMap: FieldDataMap) : FieldDataContext, KoinComponent {
    private val documents = mutableListOf<Document>()

    fun document(templatePath: String, block: DocumentBuilder.() -> Unit = {}) {
        documents += DocumentBuilder(templatePath, fieldDataMap).apply(block).build()
    }

    fun build() = documents.toList()
}

fun documentSet(block: DocumentSetContext.() -> Unit) = DocumentSet(block)
