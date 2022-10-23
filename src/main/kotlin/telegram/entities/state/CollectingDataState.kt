package telegram.entities.state

import domain.datatypes.FieldData
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
data class CollectingDataState(
    val fieldsData: List<FieldData>,
    val kClass: KClass<out FieldData>
) : DialogState
