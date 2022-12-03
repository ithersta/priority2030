package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.InformationCost
import telegram.entities.state.CostCollectorState
import telegram.resources.strings.CollectorStrings
import validation.IsMoney

fun CollectorMapBuilder.costInfoCollector() {
    collector<InformationCost>(initialState = CostCollectorState.WaitingPrice) {
        state<CostCollectorState.WaitingPrice> {
            onEnter { sendTextMessage(it, CollectorStrings.Cost.Price) }
            onText { message ->
                val price = message.content.text
                if (IsMoney(price)) {
                    val info = InformationCost(price.toDouble())
                    this@collector.exit(state, listOf(info))
                } else {
                    sendTextMessage(message.chat, CollectorStrings.Cost.IsWrongPrice)
                    return@onText
                }
            }
        }
    }
}
