package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.PurchaseCost
import telegram.entities.state.PurchaseCostState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings.InvalidPurchaseCost
import validation.IsPurchaseCostValid

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
                val totalCostInCopecks = totalCost.replaceFirst(".", "").toLongOrNull()
                if (IsPurchaseCostValid(totalCost) && totalCostInCopecks != null) {
                    val purchaseCost = PurchaseCost(totalCostInCopecks)
                    this@collector.exit(state, purchaseCost)
                } else {
                    sendTextMessage(it.chat, InvalidPurchaseCost)
                }
            }
        }
    }
}
