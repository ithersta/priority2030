package domain.documents

import domain.datatypes.FieldData
import kotlin.reflect.KClass

typealias FieldDataMap = Map<KClass<out FieldData>, FieldData>

@DocumentDsl
interface FieldDataContext {
    val fieldDataMap: FieldDataMap
}

inline fun <reified T : FieldData> FieldDataContext.get(): T {
    return fieldDataMap[T::class] as T? ?: throw MissingFieldDataException(T::class)
}
