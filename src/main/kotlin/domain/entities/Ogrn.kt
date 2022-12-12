package domain.entities

import kotlinx.serialization.Serializable

@Serializable
class IpOgrn private constructor(
    val value: String
) {
    companion object {
        fun of(value: String) = IpOgrn(value).takeIf { Regex("\\d{15}").matches(value) }
    }
}

@Serializable
class OooOgrn private constructor(
    val value: String
) {
    companion object {
        fun of(value: String) = OooOgrn(value).takeIf { Regex("\\d{13}").matches(value) }
    }
}
