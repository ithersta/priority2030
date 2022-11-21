package domain.entitties

import kotlinx.serialization.Serializable
import telegram.resources.strings.CollectorStrings

@Serializable
data class SelectionIdentifier private constructor (
    val indicator:String){
    companion object{
        fun of(indicator: String)=if (
            CollectorStrings.PurchaseDescription.SelectionIdentifier.SelectionIdentifierOptions.contains(indicator)
        ) SelectionIdentifier(indicator) else null
    }

}
