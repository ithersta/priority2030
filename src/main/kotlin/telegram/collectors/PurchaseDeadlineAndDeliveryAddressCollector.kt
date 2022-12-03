package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.PurchaseDeadlineAndDeliveryAddress
import extensions.parse
import kotlinx.datetime.LocalDate
import telegram.entities.state.PurchaseDeadlineAndDeliveryAddressState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings

fun CollectorMapBuilder.purchaseDeadlineAndDeliveryAddressCollector() {
    collector<PurchaseDeadlineAndDeliveryAddress>(
        initialState = PurchaseDeadlineAndDeliveryAddressState.WaitingForDeadline
    ) {
        state<PurchaseDeadlineAndDeliveryAddressState.WaitingForDeadline> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseDeadline) }
            onText {
                val purchaseDeadline = LocalDate.parse("dd.MM.uuuu", it.content.text)
                if (purchaseDeadline != null) {
                    state.override {
                        PurchaseDeadlineAndDeliveryAddressState.WaitingForDeliveryAddress(purchaseDeadline)
                    }
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.InvalidDate)
                }
            }
        }
        state<PurchaseDeadlineAndDeliveryAddressState.WaitingForDeliveryAddress> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseDeliveryAddress) }
            onText {
                val purchaseDeliveryAddress = it.content.text
                val purchaseDeadlineAndDeliveryAddress =
                    PurchaseDeadlineAndDeliveryAddress(
                        state.snapshot.deadline,
                        purchaseDeliveryAddress
                    )
                this@collector.exit(state, purchaseDeadlineAndDeliveryAddress)
            }
        }
    }
}
