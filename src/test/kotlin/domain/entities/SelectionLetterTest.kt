package domain.entities

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SelectionLetterTest {
    @Test
    fun of() {
        assertEquals(SelectionLetter.of("а")?.value, "а")
        assertEquals(SelectionLetter.of("о")?.value, "о")
        assertEquals(SelectionLetter.of("у")?.value, "у")
        assertEquals(SelectionLetter.of("А")?.value, "а")
        assertEquals(SelectionLetter.of("ё")?.value, "ё")
        assertEquals(SelectionLetter.of("ау"), null)
        assertEquals(SelectionLetter.of("щ"), null)
        assertEquals(SelectionLetter.of("щау"), null)
        assertEquals(SelectionLetter.of("ащ"), null)
        assertEquals(SelectionLetter.of(""), null)
    }
}
