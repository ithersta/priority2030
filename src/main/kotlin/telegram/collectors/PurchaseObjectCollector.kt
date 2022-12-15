package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.PurchaseObject
import domain.entities.ShortName
import telegram.entities.state.PurchaseObjectState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings

fun CollectorMapBuilder.purchaseObjectCollector() {
    collector<PurchaseObject>(initialState = PurchaseObjectState) {
        state<PurchaseObjectState> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseDescription.ShortName) }
            onText {
                val shortName = ShortName.of(it.content.text)
                if(shortName != null) {
                    val purchaseObject = PurchaseObject(
                        shortName
                    )
                    this@collector.exit(state, purchaseObject)
                } else{
                    sendTextMessage(it.chat,InvalidInputStrings.InvalidShortName)
                }
            }
        }
    }
}
