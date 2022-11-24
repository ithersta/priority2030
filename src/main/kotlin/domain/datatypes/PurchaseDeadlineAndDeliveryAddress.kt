package domain.datatypes

import domain.entities.Date
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseDeadlineAndDeliveryAddress(
    val deadline: Date,
    val deliveryAddress: String
):FieldData
