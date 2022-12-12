import org.junit.jupiter.api.Assertions

import validation.IsShortNameValid

internal class IsShortNameValidTest {

    @org.junit.jupiter.api.Test
    operator fun invoke() {
        Assertions.assertTrue(IsShortNameValid("услуги по выплатам материальной помощи Масайским жирафам"))
        Assertions.assertTrue(IsShortNameValid("услуги по выплатам материальной помощи жирафам Ротшильда в размере 20000 у.е."))
        Assertions.assertTrue(IsShortNameValid("услуги по выплатам материальной помощи Ангольским жирафам, предназначенным для их кормления"))
        Assertions.assertFalse(IsShortNameValid("услуги выплата южноафриканским жирафам"))
        Assertions.assertFalse(IsShortNameValid(""))
        Assertions.assertFalse(IsShortNameValid("1234 услуга по"))
        Assertions.assertFalse(IsShortNameValid("живите, жирафы..."))
        Assertions.assertFalse(IsShortNameValid("тест"))
    }
}
