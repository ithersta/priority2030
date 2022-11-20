package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseIniciator(
    val fio:String
):FieldData
