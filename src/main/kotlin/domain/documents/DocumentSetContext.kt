package domain.documents

@DocumentDsl
class DocumentSetContext(override val fieldDataMap: FieldDataMap) : FieldDataContext {
    private val documents = mutableListOf<Document>()

    fun document(templatePath: String, block: DocumentBuilder.() -> Unit = {}) {
        documents += DocumentBuilder(templatePath, fieldDataMap).apply(block).build()
    }

    fun build() = documents.toList()
}

fun documentSet(block: DocumentSetContext.() -> Unit) = DocumentSet(block)
