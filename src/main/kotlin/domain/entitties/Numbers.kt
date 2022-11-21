package domain.entitties

import kotlinx.serialization.Serializable
import validation.IsNumberValid

@Serializable
data class Numbers private constructor(
    val number:String){
    companion object{
        fun of(number: String)=if (IsNumberValid(number)) Numbers(number) else null
    }
}