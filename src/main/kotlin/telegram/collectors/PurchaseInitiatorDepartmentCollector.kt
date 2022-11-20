package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.PurchaseInitiatorDepartment
import telegram.entities.state.PurchaseInitiatorDepartmentState
import telegram.resources.strings.CollectorStrings

fun CollectorMapBuilder.purchaseInitiatorDepartmentCollector() {
    collector<PurchaseInitiatorDepartment>(initialState = PurchaseInitiatorDepartmentState) {
        state<PurchaseInitiatorDepartmentState> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseInitiatorDepartment) }
            onText {
                val purchseInitiatorDepartment = it.content.text
                val purchaseInitiator = PurchaseInitiatorDepartment(purchseInitiatorDepartment)
                this@collector.exit(state, listOf(purchaseInitiator))
            }
        }
    }
}
