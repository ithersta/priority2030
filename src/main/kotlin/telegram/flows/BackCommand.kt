package telegram.flows

import com.ithersta.tgbotapi.fsm.BaseStatefulContext
import com.ithersta.tgbotapi.fsm.builders.StateFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.IdChatIdentifier
import telegram.entities.state.DialogState
import telegram.resources.strings.Strings

fun StateFilterBuilder<DialogState, *, *, *, *>.backCommand() {
    onCommand("back", description = Strings.Help.Back) { message ->
        rollbackSafely(message.chat.id)
    }
}

suspend fun BaseStatefulContext<DialogState, *, out DialogState, *>.rollbackSafely(chatId: IdChatIdentifier) {
    if (state.rollback().not()) {
        sendTextMessage(chatId, Strings.CantRollback)
        state.override { this }
    }
}
