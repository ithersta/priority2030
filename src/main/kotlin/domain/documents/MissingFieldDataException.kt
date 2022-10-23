package domain.documents

import domain.datatypes.FieldData
import kotlin.reflect.KClass

class MissingFieldDataException(val kClass: KClass<out FieldData>) : Exception("Missing data: $kClass")
