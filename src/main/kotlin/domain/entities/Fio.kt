package domain.entities

import kotlinx.serialization.Serializable
import validation.IsFullNameValid

@Serializable
data class Fio private constructor(
    val fio:String){
    companion object{
        fun of(fio: String)=if (IsFullNameValid(fio)) Fio(fio) else null
    }
}
