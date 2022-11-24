package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.utils.row
import domain.datatypes.PurchaseDescription
import domain.entities.SelectionIdentifier
import domain.entities.SelectionLetter
import telegram.entities.state.PurchaseDescriptionState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.CollectorStrings.PurchaseDescription.MaterialValuesAreNeeded
import telegram.resources.strings.CollectorStrings.PurchaseDescription.No
import telegram.resources.strings.CollectorStrings.PurchaseDescription.Yes
import telegram.resources.strings.InvalidInputStrings
import telegram.resources.strings.infoWithLink

private val answerToBoolean = mapOf(
    No to false,
    Yes to true
)

fun CollectorMapBuilder.purchaseDescriptionCollector() {
    collector<PurchaseDescription>(initialState = PurchaseDescriptionState.WaitingForShortJustification) {
        state<PurchaseDescriptionState.WaitingForShortJustification> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseDescription.ShortJustification) }
            onText {
                state.override {
                    PurchaseDescriptionState.WaitingForSelectionLetter(it.content.text)
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
                val letter = SelectionLetter.of(it.content.text)
                if (letter != null) {
                    state.override {
                        PurchaseDescriptionState.WaitingForSelectionIdentifier(shortJustification, letter)
                    }
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.PurchaseDescription.InvalidSelectionLetter)
                    return@onText
                }
            }
        }
        state<PurchaseDescriptionState.WaitingForSelectionIdentifier> {
            onEnter { chatId ->
                sendTextMessage(
                    chatId,
                    infoWithLink(
                        CollectorStrings.PurchaseDescription.SelectionIdentifier.Question,
                        CollectorStrings.PurchaseDescription.SelectionIdentifier.ClickMe,
                        CollectorStrings.PurchaseDescription.SelectionIdentifier.Link
                    ),
                    replyMarkup = replyKeyboard(
                        resizeKeyboard = true,
                        oneTimeKeyboard = true
                    ) {
                        SelectionIdentifier.options.chunked(4).forEach {
                            row {
                                it.forEach { simpleButton(it) }
                            }
                        }
                    }
                )
            }
            onText {
                val indicator = SelectionIdentifier.of(it.content.text)
                if (indicator != null) {
                    state.override {
                        PurchaseDescriptionState.WaitingForFullJustification(
                            shortJustification,
                            selectionLetter,
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
                state.override {
                    PurchaseDescriptionState.WaitingForMaterialValuesNeed(
                        shortJustification,
                        selectionLetter,
                        selectionIdentifier,
                        it.content.text
                    )
                }
            }
        }

        state<PurchaseDescriptionState.WaitingForMaterialValuesNeed> {
            onEnter {
                sendTextMessage(
                    it,
                    MaterialValuesAreNeeded,
                    replyMarkup = replyKeyboard(
                        resizeKeyboard = true,
                        oneTimeKeyboard = true
                    ) {
                        row {
                            simpleButton(No)
                            simpleButton(Yes)
                        }
                    }
                )
            }
            onText {
                val areNeeded = answerToBoolean[it.content.text]
                if (areNeeded != null) {
                    val purchaseDescription = PurchaseDescription(
                        shortJustification = state.snapshot.shortJustification,
                        selectionLetter = state.snapshot.selectionLetter,
                        selectionIdentifier = state.snapshot.selectionIdentifier,
                        fullJustification = state.snapshot.fullJustification,
                        materialValuesAreNeeded = areNeeded
                    )
                    this@collector.exit(state, listOf(purchaseDescription))
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.InvalidAnswer)
                }
            }
        }
    }
}
