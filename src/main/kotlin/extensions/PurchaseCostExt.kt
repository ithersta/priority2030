@file:Suppress("ArrayPrimitive")
// MessageFormat doesn't work with primitive arrays

package extensions

import com.ibm.icu.text.MessageFormat
import com.ibm.icu.text.RuleBasedNumberFormat
import domain.datatypes.PurchaseCost
import java.util.*

private val locale = Locale.forLanguageTag("ru")
private val spelloutNumberFormat = RuleBasedNumberFormat(
    Locale.forLanguageTag("ru"),
    RuleBasedNumberFormat.SPELLOUT
)

fun PurchaseCost.spelloutRubles(): String = spelloutNumberFormat.format(rubles)
fun PurchaseCost.spelloutCopecks(): String = spelloutNumberFormat.format(copecks)

fun PurchaseCost.rublesUnit(): String = MessageFormat(
    "{0, plural, " +
            "one {рубль}" +
            "few {рубля}" +
            "other {рублей}}", locale
).format(arrayOf(rubles))

fun PurchaseCost.copecksUnit(): String = MessageFormat(
    "{0, plural, " +
            "one {копейка}" +
            "few {копейки}" +
            "other {копеек}}", locale
).format(arrayOf(copecks))
