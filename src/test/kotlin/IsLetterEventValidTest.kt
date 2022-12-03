import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import validation.IsLetterEventValid

internal class IsLetterEventValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsLetterEventValid("а"))
        assertTrue(IsLetterEventValid("о"))
        assertTrue(IsLetterEventValid("у"))
        assertFalse(IsLetterEventValid("ау"))
        assertFalse(IsLetterEventValid("А"))
        assertFalse(IsLetterEventValid("щ"))
        assertFalse(IsLetterEventValid("щау"))
        assertFalse(IsLetterEventValid("ащ"))
        assertFalse(IsLetterEventValid(""))
    }
}
