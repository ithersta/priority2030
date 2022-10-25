import org.junit.jupiter.api.Assertions.*
import validation.IsCorrAccountValid

internal class IsCorrAccountValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsCorrAccountValid("30112345678901234567"))
        assertFalse(IsCorrAccountValid("3212345678901234567"))
        assertFalse(IsCorrAccountValid("12345678901"))
        assertFalse(IsCorrAccountValid("sgshj"))
        assertFalse(IsCorrAccountValid("12345678901234567890"))
        assertFalse(IsCorrAccountValid("тест"))
    }
}
