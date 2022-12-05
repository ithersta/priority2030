import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import validation.IsSettlementAccountValid

internal class IsPaymentAccountValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsSettlementAccountValid("12345678901234567890"))
        assertFalse(IsSettlementAccountValid("1234567890123456789"))
        assertFalse(IsSettlementAccountValid("1234567890123456789k"))
        assertFalse(IsSettlementAccountValid("sgshj"))
        assertFalse(IsSettlementAccountValid("12345678sjksj123456789"))
        assertFalse(IsSettlementAccountValid("тест"))
        assertFalse(IsSettlementAccountValid(""))
    }
}
