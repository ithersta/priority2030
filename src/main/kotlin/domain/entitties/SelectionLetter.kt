package domain.entitties

import kotlinx.serialization.Serializable
import validation.IsLetterEventValid

@Serializable
data class SelectionLetter (
    val letter: String
){
    companion object{
        fun of(letter: String)=if (IsLetterEventValid(letter)) SelectionLetter(letter) else null
    }
}