package telegram.flows

import com.ithersta.tgbotapi.menu.builders.menu
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import telegram.entities.state.CollectingDataState
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.resources.strings.Strings

val mainMenu = menu<DialogState, Unit, _>(Strings.Menu.Message, EmptyState) {
    button(Strings.Menu.CreateDocuments) { message ->
        sendTextMessage(message.chat, Strings.CreateDocumentsMessage, replyMarkup = ReplyKeyboardRemove())
        state.override { CollectingDataState(emptyList()) }
    }
}
