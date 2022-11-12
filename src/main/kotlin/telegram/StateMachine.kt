package telegram

import com.ithersta.tgbotapi.fsm.builders.rolelessStateMachine
import com.ithersta.tgbotapi.persistence.SqliteStateRepository
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import mu.KotlinLogging
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.flows.documentBuildingLoop
import telegram.flows.mainMenu
import telegram.flows.startCommand
import telegram.resources.strings.Strings

private val logger = KotlinLogging.logger { }

val stateMachine = rolelessStateMachine(
    stateRepository = SqliteStateRepository.create<DialogState>(),
    initialState = EmptyState,
    onException = { userId, throwable ->
        logger.info(throwable) { userId }
        sendTextMessage(userId, Strings.InternalError)
    }
) {
    startCommand()
    mainMenu.run { invoke() }
    documentBuildingLoop()
}
