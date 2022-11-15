package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import domain.datatypes.PurchaseDescription
import domain.datatypes.PurchaseIniciator
import telegram.entities.state.PurchaseDescriptionState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings
import validation.IsLetterEventValid

fun CollectorMapBuilder.purchaseDescriptionCollector() {
    collector<PurchaseDescription>(initialState = PurchaseDescriptionState.WaitingForShortName) {
        state<PurchaseDescriptionState.WaitingForShortName> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseDescription.ShortName) }
            onText { state.override { PurchaseDescriptionState.WaitingForShortJustification(it.content.text) } }
        }
        state<PurchaseDescriptionState.WaitingForShortJustification> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseDescription.ShortJustification) }
            onText {
                state.override {
                    PurchaseDescriptionState.WaitingForSelectionLetter(
                        state.snapshot.shortName,
                        it.content.text
                    )
                }
            }
        }
        state<PurchaseDescriptionState.WaitingForSelectionLetter> {
            onEnter {
                sendTextMessage(
                    it,
                    CollectorStrings.PurchaseDescription.SelectionLetter
                )
            }
            onText {
                val letter = it.content.text
                if (IsLetterEventValid(letter)) {
                    state.override {
                        PurchaseDescriptionState.WaitingForSelectionIdentifier(
                            state.snapshot.shortName,
                            state.snapshot.shortJustification,
                            letter
                        )
                    }
                } else {
                    SendTextMessage(it.chat.id, InvalidInputStrings.PurchaseDescription.InvalidSelectionLetter)
                }

            }
        }
    }
}
