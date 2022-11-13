import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import validation.IsIndicatorValid

internal class IsIndicatorValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsIndicatorValid("1"))
        assertTrue(IsIndicatorValid("12"))
        assertTrue(IsIndicatorValid("-"))
        assertFalse(IsIndicatorValid("12-"))
        assertFalse(IsIndicatorValid("-12"))
        assertFalse(IsIndicatorValid("1-2"))
        assertFalse(IsIndicatorValid("--"))
        assertFalse(IsIndicatorValid("--123"))
        assertFalse(IsIndicatorValid(""))
    }
}
