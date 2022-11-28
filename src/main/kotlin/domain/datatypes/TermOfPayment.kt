package domain.datatypes

import kotlinx.serialization.Serializable

@Serializable
enum class TermOfPayment : FieldData {
    Prepaid, Fact, Partially
}
