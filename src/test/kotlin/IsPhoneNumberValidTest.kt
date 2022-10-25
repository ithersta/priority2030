import org.junit.jupiter.api.Assertions.*
import validation.IsPhoneNumberValid

internal class IsPhoneNumberValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsPhoneNumberValid("+78005553535"))
        assertFalse(IsPhoneNumberValid("+7 800 5553535"))
        assertFalse(IsPhoneNumberValid("88005553535"))
        assertFalse(IsPhoneNumberValid("+799030"))
        assertFalse(IsPhoneNumberValid("+7929299299929299229"))
        assertFalse(IsPhoneNumberValid("тест"))
    }
}
