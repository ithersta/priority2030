import org.junit.jupiter.api.Assertions.*
import validation.IsNumberValid

internal class IsNumberValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsNumberValid("1"))
        assertTrue(IsNumberValid("20920924858"))
        assertTrue(IsNumberValid("70"))
        assertFalse(IsNumberValid("-1"))
        assertFalse(IsNumberValid("71d200"))
        assertFalse(IsNumberValid("1234567-890k2345"))
    }
}
