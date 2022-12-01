package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.flatReplyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import domain.datatypes.RussianFullName
import telegram.entities.state.FullNameCollectorState
import telegram.resources.strings.CollectorStrings

fun CollectorMapBuilder.fullNameCollector() {
    collector<RussianFullName>(initialState = FullNameCollectorState.WaitingForLastName) {
        state<FullNameCollectorState.WaitingForLastName> {
            onEnter { sendTextMessage(it, CollectorStrings.FullName.lastName) }
            onText { state.override { FullNameCollectorState.WaitingForFirstName(it.content.text) } }
        }
        state<FullNameCollectorState.WaitingForFirstName> {
            onEnter { sendTextMessage(it, CollectorStrings.FullName.firstName) }
            onText { state.override { FullNameCollectorState.WaitingForPatronymic(lastName, it.content.text) } }
        }
        state<FullNameCollectorState.WaitingForPatronymic> {
            onEnter { chatId ->
                sendTextMessage(
                    chatId = chatId,
                    text = CollectorStrings.FullName.patronymic,
                    replyMarkup = flatReplyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                        simpleButton(CollectorStrings.FullName.noPatronymic)
                    }
                )
            }
            onText { message ->
                val name = RussianFullName(
                    lastName = state.snapshot.lastName,
                    firstName = state.snapshot.firstName,
                    patronymic = message.content.text.takeIf { it != CollectorStrings.FullName.noPatronymic }
                )
                this@collector.exit(state, listOf(name))
            }
        }
    }
}
