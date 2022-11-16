package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.PurchaseDescription
import telegram.entities.state.PurchaseDescriptionState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings
import telegram.resources.strings.infoWithLink
import validation.IsIndicatorValid
import validation.IsLetterEventValid

fun CollectorMapBuilder.purchaseDescriptionCollector() {
    collector<PurchaseDescription>(initialState = PurchaseDescriptionState.WaitingForShortJustification) {
        state<PurchaseDescriptionState.WaitingForShortJustification> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseDescription.ShortJustification) }
            onText {
                state.override {
                    PurchaseDescriptionState.WaitingForSelectionLetter(
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
                            state.snapshot.shortJustification,
                            letter
                        )
                    }
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.PurchaseDescription.InvalidSelectionLetter)
                    return@onText
                }
            }
        }
        state<PurchaseDescriptionState.WaitingForSelectionIdentifier> {
            onEnter {
                sendTextMessage(
                    it,
                    infoWithLink(
                        CollectorStrings.PurchaseDescription.SelectionIdentifier.Question,
                        CollectorStrings.PurchaseDescription.SelectionIdentifier.ClickMe,
                        CollectorStrings.PurchaseDescription.SelectionIdentifier.Link
                    )
                )
            }
            onText {
                val indicator = it.content.text
                if (IsIndicatorValid(indicator)) {
                    state.override {
                        PurchaseDescriptionState.WaitingForFullJustification(
                            state.snapshot.shortJustification,
                            state.snapshot.selectionLetter,
                            indicator
                        )
                    }
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.PurchaseDescription.InvalidSelectionIdentifier)
                    return@onText
                }
            }
        }
        state<PurchaseDescriptionState.WaitingForFullJustification> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseDescription.FullJustification) }
            onText {
                val purchaseDescription = PurchaseDescription(
                    shortJustification = state.snapshot.shortJustification,
                    selectionLetter = state.snapshot.selectionLetter,
                    selectionIdentifier = state.snapshot.selectionIdentifier,
                    fullJustification = it.content.text
                )
                this@collector.exit(state, listOf(purchaseDescription))
            }
        }
    }
}
