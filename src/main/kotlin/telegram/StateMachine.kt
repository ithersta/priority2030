package telegram

import com.ithersta.tgbotapi.fsm.builders.rolelessStateMachine
import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.repository.InMemoryStateRepositoryImpl
import com.ithersta.tgbotapi.menu.builders.menu
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.UserId
import telegram.entities.state.CollectingDataState
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState

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
        onEnter {

        }
    }
}
