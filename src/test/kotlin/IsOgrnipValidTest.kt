import org.junit.jupiter.api.Assertions.*
import validation.IsOgrnipValid

internal class IsOgrnipValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        assertTrue(IsOgrnipValid("123456789012345 от 22.10.2022"))
        assertFalse(IsOgrnipValid("123456789012345от22.10.2022"))
        assertFalse(IsOgrnipValid("123456789012345 от 22.10.22"))
        assertFalse(IsOgrnipValid("1234567890k2345 от 22.10.22"))
        assertFalse(IsOgrnipValid("sgshj"))
        assertFalse(IsOgrnipValid("12345678sjksj123456789"))
        assertFalse(IsOgrnipValid("тест"))
        assertFalse(IsOgrnipValid(""))
    }
}
