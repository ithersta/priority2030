import org.junit.jupiter.api.Assertions.*
import validation.IsFullNameValid

internal class IsFullNameValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsFullNameValid("В.С. Душечкина"))
        assertFalse(IsFullNameValid("В.С Душечкина"))
        assertFalse(IsFullNameValid("Душечкина"))
        assertFalse(IsFullNameValid("Душечкина В.С"))
        assertFalse(IsFullNameValid("Душечкина В.Сергеевна"))
        assertFalse(IsFullNameValid("Душечкина Виктория Сергеевна"))
        assertFalse(IsFullNameValid(""))
    }
}
