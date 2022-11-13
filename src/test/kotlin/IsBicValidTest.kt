import org.junit.jupiter.api.Assertions.*
import validation.IsBicValid

internal class IsBicValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsBicValid("123456789"))
        assertFalse(IsBicValid("1234567890123456789"))
        assertFalse(IsBicValid("k1234567890123456789k"))
        assertFalse(IsBicValid("sgshj"))
        assertFalse(IsBicValid("12345678sjksj123456789"))
        assertFalse(IsBicValid("тест"))
        assertFalse(IsBicValid(""))
    }
}
