package telegram.entities.state

import domain.datatypes.FieldData
import kotlinx.serialization.Serializable

@Serializable
data class CollectingDataState(
    val fieldsData: List<FieldData>
) : DialogState
