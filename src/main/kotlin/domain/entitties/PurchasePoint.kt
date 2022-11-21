package domain.entitties

import kotlinx.serialization.Serializable
import validation.IsPointNumberValid

@Serializable
data class PurchasePoints (
    val point:String){
    companion object{
        fun of(point: String)=if (IsPointNumberValid(point)) PurchasePoints(point) else null
    }
}

