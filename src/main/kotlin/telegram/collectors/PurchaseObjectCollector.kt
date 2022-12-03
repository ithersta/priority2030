package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.PurchaseObject
import telegram.entities.state.PurchaseObjectState
import telegram.resources.strings.CollectorStrings

fun CollectorMapBuilder.purchaseObjectCollector() {
    collector<PurchaseObject>(initialState = PurchaseObjectState) {
        state<PurchaseObjectState> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseDescription.ShortName) }
            onText {
                val purchaseObj = PurchaseObject(it.content.text)
                this@collector.exit(state, purchaseObj)
            }
        }
    }
}
