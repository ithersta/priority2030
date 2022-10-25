import org.junit.jupiter.api.Assertions.*
import validation.IsEmailValid

internal class IsEmailValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsEmailValid("dushechkina.vs@edu.spbstu.ru"))
        assertTrue(IsEmailValid("dushechkina@mail.ru"))
        assertFalse(IsEmailValid("dushechkina.vsedu.spbstu.ru"))
        assertFalse(IsEmailValid("ddddddddddddddddddddd"))
        assertFalse(IsEmailValid("тест"))
    }
}
