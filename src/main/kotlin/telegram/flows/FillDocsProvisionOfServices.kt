package telegram.flows

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import telegram.entities.state.DialogState
import telegram.entities.state.FillingProvisionOfServicesState
import telegram.entities.state.FillingProvisionOfServicesState.BeginningFillDoc
import telegram.resources.strings.ButtonStrings
import telegram.resources.strings.Strings

fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.FillDocsProvisionOfServices() {
    state<BeginningFillDoc>{
        button(ButtonStrings.ChoiceFillingDoc.ProvisionOfServices) { message ->
            sendTextMessage(message.chat, Strings.CreateDocumentsMessage, replyMarkup = ReplyKeyboardRemove())
            state.override { FillingProvisionOfServicesState.BeginningFillDoc}
        }
        button(ButtonStrings.ChoiceFillingDoc.ProvisionOfServices) { message ->
            sendTextMessage(message.chat, Strings.CreateDocumentsMessage, replyMarkup = ReplyKeyboardRemove())
            state.override { FillingProvisionOfServicesState.BeginningFillDoc}
        }
    }
}