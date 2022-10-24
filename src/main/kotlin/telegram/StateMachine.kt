package telegram

import com.ithersta.tgbotapi.fsm.builders.stateMachine
import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.repository.InMemoryStateRepositoryImpl
import com.ithersta.tgbotapi.fsm.setState
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.UserId
import documentSet
import domain.datatypes.FieldData
import domain.documents.DocumentSet
import telegram.entities.state.CollectingDataState
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState

val stateMachine = stateMachine(
    getUser = {},
    stateRepository = InMemoryStateRepositoryImpl<UserId, DialogState>(EmptyState),
) {
    role {
        state<CollectingDataState> {
            onEnter {
                TODO()
            }
            onText { message ->
                TODO()
            }
        }
    }
}
