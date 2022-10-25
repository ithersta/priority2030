import org.junit.jupiter.api.Assertions.*
import validation.IsPaymentAccountValid

internal class IsPaymentAccountValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsPaymentAccountValid("12345678901234567890"))
        assertFalse(IsPaymentAccountValid("1234567890123456789"))
        assertFalse(IsPaymentAccountValid("1234567890123456789k"))
        assertFalse(IsPaymentAccountValid("sgshj"))
        assertFalse(IsPaymentAccountValid("12345678sjksj123456789"))
        assertFalse(IsPaymentAccountValid("тест"))
    }
}
