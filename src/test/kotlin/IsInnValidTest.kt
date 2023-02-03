import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import validation.IsInnValidForIp

internal class IsInnValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsInnValidForIp("123456789012"))
        assertFalse(IsInnValidForIp("123"))
        assertFalse(IsInnValidForIp("12345678901"))
        assertFalse(IsInnValidForIp("sgshj"))
        assertFalse(IsInnValidForIp("12345678901234567890"))
        assertFalse(IsInnValidForIp("тест"))
        assertFalse(IsInnValidForIp(""))
    }
}
