import org.junit.jupiter.api.Assertions.*
import validation.IsPointNumberValid

internal class IsPointNumberValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsPointNumberValid("1"))
        assertTrue(IsPointNumberValid("2"))
        assertTrue(IsPointNumberValid("70"))
        assertFalse(IsPointNumberValid("71"))
        assertFalse(IsPointNumberValid("0"))
        assertFalse(IsPointNumberValid("1234567890k2345"))
        assertFalse(IsPointNumberValid("702"))
        assertFalse(IsPointNumberValid("12345678sjksj123456789"))
        assertFalse(IsPointNumberValid("тест"))
    }
}
