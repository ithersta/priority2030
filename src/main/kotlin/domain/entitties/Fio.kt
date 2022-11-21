package domain.entitties

import kotlinx.serialization.Serializable
import validation.IsFullNameValid

@Serializable
data class Fio (
    val fio:String){
    companion object{
        fun of(fio: String)=if (IsFullNameValid(fio)) Fio(fio) else null
    }
}