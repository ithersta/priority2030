package domain.datatypes

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseDeadlineAndDeliveryAddress(
    val deadline: LocalDate,
    val deliveryAddress: String
) : FieldData
