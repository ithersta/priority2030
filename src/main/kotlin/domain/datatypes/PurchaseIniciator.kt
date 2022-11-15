package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseIniciator(
    val FIO:String
):FieldData
