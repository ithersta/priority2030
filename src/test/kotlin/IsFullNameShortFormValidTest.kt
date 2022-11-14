import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import validation.IsFullNameShortFormValid

internal class IsFullNameShortFormValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsFullNameShortFormValid("В.С. Душечкина"))
        assertFalse(IsFullNameShortFormValid("В.С Душечкина"))
        assertFalse(IsFullNameShortFormValid("Душечкина"))
        assertFalse(IsFullNameShortFormValid("Душечкина В.С"))
        assertFalse(IsFullNameShortFormValid("Душечкина В.Сергеевна"))
        assertFalse(IsFullNameShortFormValid("Душечкина Виктория Сергеевна"))
        assertFalse(IsFullNameShortFormValid(""))
    }

}
