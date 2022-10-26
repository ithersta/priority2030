package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.flatReplyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import domain.datatypes.FullName
import telegram.entities.state.FullNameCollectorState

fun CollectorMapBuilder.fullNameCollector() {
    collector<FullName.Russian>(initialState = FullNameCollectorState.WaitingForLastName) {
        state<FullNameCollectorState.WaitingForLastName> {
            onEnter { sendTextMessage(it, "Фамилия?") }
            onText { state.override { FullNameCollectorState.WaitingForFirstName(it.content.text) } }
        }
        state<FullNameCollectorState.WaitingForFirstName> {
            onEnter { sendTextMessage(it, "Имя?") }
            onText { state.override { FullNameCollectorState.WaitingForPatronymic(lastName, it.content.text) } }
        }
        state<FullNameCollectorState.WaitingForPatronymic> {
            onEnter { chatId ->
                sendTextMessage(chatId, "Отчество?", replyMarkup = flatReplyKeyboard {
                    simpleButton("Отсутствует")
                })
            }
            onText { message ->
                val name = FullName.Russian(
                    lastName = state.snapshot.lastName,
                    firstName = state.snapshot.firstName,
                    patronymic = message.content.text.takeIf { it != "Отсутствует" }
                )
                this@collector.exit(state, listOf(name))
            }
        }
    }
}
