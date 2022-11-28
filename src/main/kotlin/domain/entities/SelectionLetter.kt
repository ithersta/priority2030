package domain.entities

import kotlinx.serialization.Serializable
import validation.IsLetterEventValid

@Serializable
data class SelectionLetter private constructor (
    val letter: String
){
    companion object{
        fun of(letter: String)=if (IsLetterEventValid(letter)) SelectionLetter(letter) else null
    }
}
