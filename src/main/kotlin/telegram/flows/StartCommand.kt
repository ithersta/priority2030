package telegram.flows

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.UserId
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.resources.strings.Strings

fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.startCommand() {
    state<EmptyState> {
        onCommand("start", null) { message ->
            sendTextMessage(message.chat, Strings.Welcome)
            refreshCommands()
            state.override { this }
        }
    }
}
