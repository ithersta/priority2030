package domain.datatypes

import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

const val COPECKS_IN_RUBLE = 100

@Serializable
data class PurchaseCost(
    val value: Long
) : FieldData {
    val rubles get() = value / COPECKS_IN_RUBLE
    val copecks get() = value % COPECKS_IN_RUBLE

    operator fun times(other: BigDecimal) =
        PurchaseCost(other.multiply(value.toBigDecimal()).setScale(0, RoundingMode.HALF_UP).toLong())

    operator fun minus(other: PurchaseCost) =
        PurchaseCost(this.value-other.value)
}
