import org.junit.jupiter.api.Assertions

import validation.IsPurchaseCostValid

internal class IsPurchaseCostValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        Assertions.assertTrue(IsPurchaseCostValid("1.00"))
        Assertions.assertTrue(IsPurchaseCostValid("200.00"))
        Assertions.assertTrue(IsPurchaseCostValid("70.99"))
        Assertions.assertFalse(IsPurchaseCostValid("71.000"))
        Assertions.assertFalse(IsPurchaseCostValid(""))
        Assertions.assertFalse(IsPurchaseCostValid("12.34567890k2345"))
        Assertions.assertFalse(IsPurchaseCostValid("12345678sjks.--"))
        Assertions.assertFalse(IsPurchaseCostValid("тест"))
    }
}
