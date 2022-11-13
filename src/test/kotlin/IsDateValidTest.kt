import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import validation.IsDateValid

internal class IsDateValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsDateValid("27.08.2022"))
        assertFalse(IsDateValid("27.08.02"))
        assertFalse(IsDateValid("27 августа 2002"))
        assertFalse(IsDateValid("1111111"))
        assertFalse(IsDateValid("тест"))
        assertFalse(IsDateValid("2002"))
        assertFalse(IsDateValid(""))
    }
}
