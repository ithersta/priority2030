package services.morpher

import domain.entities.MorphedFullName
import org.jruby.embed.PathType
import org.jruby.embed.ScriptingContainer
import org.koin.core.annotation.Single

@Single
class Petrovich {
    private val container = ScriptingContainer()
    private val receiver = container.runScriptlet(PathType.CLASSPATH, "entrypoint.rb")

    fun morphFullName(fullName: String): MorphedFullName {
        val tokens = fullName.split(" ", limit = 3)
        val surname = tokens.getOrNull(0)
        val name = tokens.getOrNull(1)
        val patronymic = tokens.getOrNull(2)
        val genitive =
            container.callMethod(receiver, "genitive", arrayOf<Any?>(surname, name, patronymic), String::class.java)
        return MorphedFullName(fullName, name.orEmpty(), surname.orEmpty(), patronymic.orEmpty(), genitive)
    }
}
