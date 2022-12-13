package domain.entities

import kotlinx.serialization.Serializable
import org.apache.commons.validator.routines.EmailValidator

@JvmInline
@Serializable
value class Email private constructor(
    val email: String
) {
    companion object {
        private val emailValidator = EmailValidator.getInstance()
        fun of(email: String) = if (emailValidator.isValid(email)) Email(email) else null
    }
}
