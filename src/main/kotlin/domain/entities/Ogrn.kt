package domain.entities

import kotlinx.serialization.Serializable
import validation.IsOgrnipValidForIp
import validation.IsOgrnipValidForOoo

@Serializable
class IpOgrn private constructor(
    val value: String
) {
    companion object {
        fun of(value: String) = if (IsOgrnipValidForIp(value)) IpOgrn(value) else null
    }
}

@Serializable
class OooOgrn private constructor(
    val value: String
) {
    companion object {
        fun of(value: String) = if (IsOgrnipValidForOoo(value)) OooOgrn(value) else null
    }
}
