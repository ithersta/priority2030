package telegram

import com.ithersta.tgbotapi.fsm.builders.rolelessStateMachine
import com.ithersta.tgbotapi.fsm.repository.InMemoryStateRepositoryImpl
import dev.inmo.tgbotapi.types.UserId
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.flows.documentBuildingLoop
import telegram.flows.mainMenu
import telegram.flows.startCommand

val stateMachine = rolelessStateMachine(
    stateRepository = InMemoryStateRepositoryImpl<UserId, DialogState>(),
    initialState = EmptyState
) {
    startCommand()
    mainMenu.run { invoke() }
    documentBuildingLoop()
}
