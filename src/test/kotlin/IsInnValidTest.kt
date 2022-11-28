import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import validation.IsInnValid

internal class IsInnValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsInnValid("123456789012"))
        assertFalse(IsInnValid("123"))
        assertFalse(IsInnValid("12345678901"))
        assertFalse(IsInnValid("sgshj"))
        assertFalse(IsInnValid("12345678901234567890"))
        assertFalse(IsInnValid("тест"))
        assertFalse(IsInnValid(""))
    }
}
