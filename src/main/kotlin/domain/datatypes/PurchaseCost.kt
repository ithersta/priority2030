package domain.datatypes

import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class PurchaseCost(
    val value: Long
) : FieldData {
    val rubles get() = value / 100
    val copecks get() = value % 100

    operator fun times(other: BigDecimal) = PurchaseCost(other.multiply(value.toBigDecimal()).longValueExact())
}
