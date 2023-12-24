package telegram

import com.ithersta.tgbotapi.commands.cancelCommand
import com.ithersta.tgbotapi.commands.fallback
import com.ithersta.tgbotapi.fsm.builders.rolelessStateMachine
import dev.inmo.tgbotapi.types.UserId
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.flows.*

val stateMachine = rolelessStateMachine<DialogState, UserId>(
    initialState = EmptyState,
    includeHelp = true
) {
    cancelCommand(EmptyState)
    startCommand()
    anyState { backCommand() }
    mainMenu.run { invoke() }
    documentBuildingLoop()
    fillDocsProvisionOfServices()
    afterDocsGenerationFlow()
    fallback()
}
