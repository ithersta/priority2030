package services.morpher

import domain.entities.MorphedFullName
import org.koin.core.annotation.Single

@Single
class Morpher(private val webMorpher: WebMorpher, private val petrovich: Petrovich) {
    fun morphFullName(fullName: String): MorphedFullName {
        return webMorpher.morphFullName(fullName) ?: petrovich.morphFullName(fullName)
    }
}
