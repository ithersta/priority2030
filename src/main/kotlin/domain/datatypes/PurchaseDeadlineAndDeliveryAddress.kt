package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseDeadlineAndDeliveryAddress(
    val deadline: String,
    val deliveryAddress: String
):FieldData