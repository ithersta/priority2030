package domain.entities

import kotlinx.serialization.Serializable
import validation.IsInnValidForIp
import validation.IsInnValidForOoo

@Serializable
class IpInn private constructor(
    val value: String
) {
    companion object {
        fun of(value: String) = if (IsInnValidForIp(value)) IpInn(value) else null
    }
}

@Serializable
class OooInn private constructor(
    val value: String
) {
    companion object {
        fun of(value: String) = if (IsInnValidForOoo(value)) OooInn(value) else null
    }
}
