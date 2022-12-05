package domain.documents

import org.koin.core.component.KoinComponent

@DocumentDsl
class DocumentBuilder(
    private val templatePath: String,
    override val fieldDataMap: FieldDataMap
) : FieldDataContext, KoinComponent {
    private val replacements = mutableListOf<Pair<String, String>>()

    infix fun String.replaceWith(value: String) {
        replacements += this to value
    }

    fun field(marker: String, value: String) {
        replacements += marker to value
    }

    fun build() = Document(templatePath, replacements.toList())
}

