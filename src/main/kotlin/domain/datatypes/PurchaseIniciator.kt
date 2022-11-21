package domain.datatypes

import domain.entitties.Fio
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseIniciator(
    val fio: Fio
):FieldData
