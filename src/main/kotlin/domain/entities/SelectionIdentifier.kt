package domain.entities

import kotlinx.serialization.Serializable

@Serializable
class SelectionIdentifier private constructor(
    val indicator: String
) {
    companion object {
        val options = setOf(
            "ПРГ1", "ПРГ2", "Р1(б)", "Р2(б)", "Р3(б)", "Р4(б)", "Р5(б)", "Р6(б)", "Р1(с1)",
            "Р2(с1)", "Р3(с1)", "Р4(с1)", "Р5(с1)", "Р6(с1)", "Р7(с1)_1", "Р7(с1)_2", "Р8(с1)_1", "Р8(с1)_2", "-"
        )

        fun of(indicator: String) = if (indicator in options) SelectionIdentifier(indicator) else null
    }
}
