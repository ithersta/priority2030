package domain.entities

import kotlinx.serialization.Serializable
import validation.IsDateValid

@Serializable
data class Date private constructor(
    val date:String){
    companion object{
        fun of(date: String)=if (IsDateValid(date)) Date(date) else null
    }
}
