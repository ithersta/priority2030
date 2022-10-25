package telegram

import com.ithersta.tgbotapi.fsm.builders.rolelessStateMachine
import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.repository.InMemoryStateRepositoryImpl
import com.ithersta.tgbotapi.menu.builders.menu
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.flatReplyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.types.UserId
import domain.datatypes.FullName
import telegram.entities.state.CollectingDataState
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.entities.state.FullNameCollectorState

val stateMachine = rolelessStateMachine(
    stateRepository = InMemoryStateRepositoryImpl<UserId, DialogState>(),
    initialState = EmptyState
) {
    state<EmptyState> {
        onCommand("start", null) { message ->
            state.override { this }
        }
    }
    menu<DialogState, Unit, _>("Выберите действие", EmptyState) {
        button("Собрать документ", CollectingDataState(emptyList()))
    }.run { invoke() }
    state<CollectingDataState> {
        nestedStateMachine(
            onExit = { copy(fieldsData = fieldsData + it) }
        ) {
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
                    this@nestedStateMachine.exit(state, name)
                }
            }
        }
        onEnter { chatId ->
            sendTextMessage(chatId, state.snapshot.toString())
            state.push(FullNameCollectorState.WaitingForLastName)
        }
    }
}
