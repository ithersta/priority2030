package domain.entities

import kotlinx.serialization.Serializable
import validation.IsPhoneNumberValid

@Serializable
data class PhoneNumber private constructor(
    val number:String){
    companion object{
        fun of(number: String)=if (IsPhoneNumberValid(number)) PhoneNumber(number) else null
    }
}
