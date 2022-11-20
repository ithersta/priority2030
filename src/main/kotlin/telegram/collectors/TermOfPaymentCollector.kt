package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.utils.row
import domain.datatypes.TermOfPayment
import telegram.entities.state.TermOfPaymentState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings

fun CollectorMapBuilder.termOfPaymentCollector() {
    collector<TermOfPayment>(initialState = TermOfPaymentState) {
        state<TermOfPaymentState> {
            onEnter {
                sendTextMessage(it,
                    CollectorStrings.TermOfPayment.Question,
                    replyMarkup = replyKeyboard(
                        resizeKeyboard = true,
                        oneTimeKeyboard = true
                    ) {
                        row {
                            simpleButton(CollectorStrings.TermOfPayment.Prepaid)
                        }
                        row {
                            simpleButton(CollectorStrings.TermOfPayment.Fact)
                        }
                        row {
                            simpleButton(CollectorStrings.TermOfPayment.Partially)
                        }
                    })
            }
            onText { message ->
                val type = when (message.content.text) {
                    CollectorStrings.TermOfPayment.Prepaid -> TermOfPayment.Prepaid
                    CollectorStrings.TermOfPayment.Fact -> TermOfPayment.Fact
                    CollectorStrings.TermOfPayment.Partially -> TermOfPayment.Partially
                    else -> {
                        sendTextMessage(message.chat, InvalidInputStrings.InvalidTermOfPayment)
                        return@onText
                    }
                }
                this@collector.exit(state, listOf(type))
            }
        }
    }
}