package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.PurchaseIniciator
import telegram.entities.state.PurchaseIniciatorState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings
import validation.IsFullNameValid

fun CollectorMapBuilder.purchaseIniciatorCollector() {
    collector<PurchaseIniciator>(initialState = PurchaseIniciatorState) {
        state<PurchaseIniciatorState> {
            onEnter {
                sendTextMessage(
                    it,
                    CollectorStrings.PurchaseIniciator
                )
            }
            onText {
                val fio = it.content.text
                if (IsFullNameValid(fio)) {
                    val inic = PurchaseIniciator(fio)
                    this@collector.exit(state, listOf(inic))
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.Invalidfio)
                    return@onText
                }
            }
        }
    }
}
