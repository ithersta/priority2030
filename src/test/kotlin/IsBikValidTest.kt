import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import validation.IsBikValid

internal class IsBikValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsBikValid("123456789"))
        assertFalse(IsBikValid("1234567890123456789"))
        assertFalse(IsBikValid("k1234567890123456789k"))
        assertFalse(IsBikValid("sgshj"))
        assertFalse(IsBikValid("12345678sjksj123456789"))
        assertFalse(IsBikValid("тест"))
        assertFalse(IsBikValid(""))
    }
}
