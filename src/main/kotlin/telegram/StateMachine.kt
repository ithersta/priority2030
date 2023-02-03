package telegram

import com.ithersta.tgbotapi.commands.cancelCommand
import com.ithersta.tgbotapi.commands.fallback
import com.ithersta.tgbotapi.fsm.builders.rolelessStateMachine
import com.ithersta.tgbotapi.persistence.SqliteStateRepository
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import mu.KotlinLogging
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.flows.*
import telegram.resources.strings.Strings

private val logger = KotlinLogging.logger { }

val stateMachine = rolelessStateMachine(
    stateRepository = SqliteStateRepository.create<DialogState>(historyDepth = 10),
    initialState = EmptyState,
    onException = { userId, throwable ->
        logger.info(throwable) { userId }
        sendTextMessage(userId, Strings.InternalError)
    },
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
