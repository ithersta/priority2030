package domain.documents

import domain.datatypes.FieldData
import kotlin.reflect.KClass

class DocumentSet(
    private val block: DocumentSetContext.() -> Unit
) {
    sealed interface Result {
        data class OK(val documents: List<Document>) : Result
        data class MissingData(val kClass: KClass<out FieldData>) : Result
    }

    fun build(fieldDataMap: FieldDataMap) = try {
        val documents = DocumentSetContext(fieldDataMap).apply(block).build()
        Result.OK(documents)
    } catch (e: MissingFieldDataException) {
        Result.MissingData(e.kClass)
    }
}
