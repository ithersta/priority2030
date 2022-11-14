import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import validation.IsEmailValid

internal class IsEmailValidTest {
    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsEmailValid("popov.gleb@mail.ru"))
        assertFalse(IsEmailValid("13123k23"))
        assertFalse(IsEmailValid("email@a."))
        assertFalse(IsEmailValid("ewkekwkqe"))
        assertFalse(IsEmailValid("@aeqe"))
    }
}