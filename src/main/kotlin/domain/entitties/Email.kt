package domain.entitties

import kotlinx.serialization.Serializable
import org.apache.commons.validator.routines.EmailValidator

@Serializable
data class Email private constructor(
    val email:String){
    companion object{
        val emailValidator = EmailValidator.getInstance()
        fun of(email: String)=if (emailValidator.isValid(email)) Email(email) else null
    }
}
