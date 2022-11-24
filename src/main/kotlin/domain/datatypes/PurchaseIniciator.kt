package domain.datatypes

import domain.entities.Fio
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseIniciator(
    val fio: Fio
):FieldData
