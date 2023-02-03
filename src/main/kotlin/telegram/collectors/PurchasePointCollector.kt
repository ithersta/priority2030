package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.PurchasePoint
import domain.entities.PurchasePoints
import telegram.entities.state.PurchasePointState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings
import telegram.resources.strings.infoWithLink

fun CollectorMapBuilder.purchasePointCollector() {
    collector<PurchasePoint>(initialState = PurchasePointState) {
        state<PurchasePointState> {
            onEnter {
                sendTextMessage(
                    it,
                    infoWithLink(
                        CollectorStrings.PurchasePoint.Question,
                        CollectorStrings.PurchasePoint.ClickMe,
                        CollectorStrings.PurchasePoint.Link
                    )
                )
            }
            onText {
                val point = PurchasePoints.of(it.content.text)
                if (point != null) {
                    val purchasePoint = PurchasePoint(number = point)
                    this@collector.exit(state, purchasePoint)
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.InvalidPurchasePoint)
                    return@onText
                }
            }
        }
    }
}
