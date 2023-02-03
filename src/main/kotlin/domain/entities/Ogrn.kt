package domain.entities

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class IpOgrn private constructor(
    val value: String
) {
    companion object {
        fun of(value: String) = IpOgrn(value).takeIf { Regex("\\d{15}").matches(value) }
    }
}

@JvmInline
@Serializable
value class OooOgrn private constructor(
    val value: String
) {
    companion object {
        fun of(value: String) = OooOgrn(value).takeIf { Regex("\\d{13}").matches(value) }
    }
}
