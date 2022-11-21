package domain.entitties

import kotlinx.serialization.Serializable
import validation.IsPhoneNumberValid

@Serializable
data class PhoneNumber (
    val number:String){
    companion object{
        fun of(number: String)=if (IsPhoneNumberValid(number)) PhoneNumber(number) else null
    }
}