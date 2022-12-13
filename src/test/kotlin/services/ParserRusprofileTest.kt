package services

import domain.entities.IpOgrn
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ParserRusprofileTest {
    @Test
    fun parseWebPage() {
        val date = ParserRusprofile().parseWebPage(IpOgrn.of("321554300064704")!!)
        assertEquals(date, LocalDate.parse("2021-10-07"))
    }
}
