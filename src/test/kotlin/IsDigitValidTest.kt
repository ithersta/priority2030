import org.junit.jupiter.api.Assertions.*
import validation.IsDigitValid

internal class IsDigitValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsDigitValid("1"))
        assertTrue(IsDigitValid("20920924858"))
        assertTrue(IsDigitValid("70"))
        assertTrue(IsDigitValid("-1"))
        assertTrue(IsDigitValid("-1.0"))
        assertTrue(IsDigitValid("1.05"))
        assertTrue(IsDigitValid("-1.66"))
        assertFalse(IsDigitValid("71d200"))
        assertFalse(IsDigitValid("1234567-890k2345"))
    }
}
