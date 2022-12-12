package services

import domain.entities.IpOgrn
import extensions.format
import org.junit.jupiter.api.Test

class ParserRusprofileTest {
    @Test
    fun parseWebPage() {
        val date = ParserRusprofile().parseWebPage(IpOgrn.of("304182813200102")!!)
        println(date?.format("dd.MM.uuuu"))
    }
}
