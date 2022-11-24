package telegram.collectors

import com.ibm.icu.text.MessageFormat
import com.ibm.icu.text.RuleBasedNumberFormat
import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.PurchaseCost
import domain.entities.Numbers
import telegram.entities.state.PurchaseCostState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings.InvalidPurchaseCost
import validation.IsPurchaseCostValid
import java.util.*


fun CollectorMapBuilder.purchaseCostCollector() {
    collector<PurchaseCost>(initialState = PurchaseCostState) {
        state<PurchaseCostState> {
            onEnter {
                sendTextMessage(
                    it,
                    CollectorStrings.PurchaseCost.Morpher
                )
            }

            onText {
                val totalCost = it.content.text
                if (IsPurchaseCostValid(totalCost)) {
                    val rubles = Numbers.of(totalCost.substringBefore('.'))
                    val cops = Numbers.of(totalCost.substringAfter('.'))

                    val ruPrescription = RuleBasedNumberFormat(
                        Locale.forLanguageTag("ru"),
                        RuleBasedNumberFormat.SPELLOUT
                    )
                    val rublesRu = ruPrescription.format(rubles!!.number.toInt())
                    val copsRu = ruPrescription.format(cops!!.number.toInt())

                    val rubleFormat = MessageFormat("{0, spellout} {0, plural, " +
                            "one {рубль}" +
                            "few {рубля}" +
                            "other {рублей}}", Locale.forLanguageTag("ru"))

                    val copFormat = MessageFormat("{0, spellout} {0, plural, " +
                            "one {копейка}" +
                            "few {копейки}" +
                            "other {копеек}}", Locale.forLanguageTag("ru"))

                    val rubl = rubleFormat.format(arrayOf(rubles.number.toInt()))
                    val cop = copFormat.format(arrayOf(cops.number.toInt()))

                    val purchaseCost = PurchaseCost(
                        costInRubles = rubles,
                        costInCops = cops,
                        costInRublesPrescription = rublesRu,
                        costInCopsPrescription = copsRu,
                        rubles = rubl,
                        cops = cop
                    )
                    this@collector.exit(state, listOf(purchaseCost))
                } else {
                    sendTextMessage(it.chat, InvalidPurchaseCost)
                }
            }
        }
    }
}
