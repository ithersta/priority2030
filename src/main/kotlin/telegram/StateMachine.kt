package telegram

import com.ithersta.tgbotapi.fsm.builders.rolelessStateMachine
import com.ithersta.tgbotapi.persistence.SqliteStateRepository
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.flows.*

val stateMachine = rolelessStateMachine(
    stateRepository = SqliteStateRepository.create<DialogState>(),
    initialState = EmptyState
) {
    startCommand()
    mainMenu.run { invoke() }
    documentBuildingLoop()
    fillDocsProvisionOfServices()
    downloadDocsProvisionOfServices()
}
