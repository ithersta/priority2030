import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import validation.IsFullNameValid

internal class IsFullNameValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsFullNameValid("Душечкина Виктория"))
        assertTrue(IsFullNameValid("Душечкина Виктория Сергеевна"))
        assertTrue(IsFullNameValid("Гасанова Илаха Адалет Гызы"))
        assertTrue(IsFullNameValid("Зейти Бассел"))
        assertFalse(IsFullNameValid("Dsds"))
        assertFalse(IsFullNameValid("Попов"))
    }
}
