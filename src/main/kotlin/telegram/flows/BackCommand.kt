package telegram.flows

import com.ithersta.tgbotapi.fsm.builders.StateFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import telegram.entities.state.DialogState
import telegram.resources.strings.Strings

fun StateFilterBuilder<DialogState, *, *, *, *>.backCommand() {
    onCommand("back", description = Strings.Help.Back) { message ->
        if (state.rollback().not()) {
            sendTextMessage(message.chat, Strings.CantRollback)
            state.override { this }
        }
    }
}
