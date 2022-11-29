package telegram.flows

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.utils.row
import telegram.entities.state.DialogState
import telegram.entities.state.FillingProvisionOfServicesState.BeginFillDoc
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import telegram.entities.state.CollectingDataState
import telegram.entities.state.EmptyState
import telegram.entities.state.FillingProvisionOfServicesState
import telegram.resources.strings.ButtonStrings
import telegram.resources.strings.Strings

fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.fillDocsProvisionOfServices() {
    state<BeginFillDoc> {
        onEnter { chatId ->
            sendTextMessage(
                chatId,
                Strings.CreateDocumentsMessage,
                replyMarkup = replyKeyboard(
                    resizeKeyboard = true,
                    oneTimeKeyboard = true
                ) {
                    row {
                        simpleButton(Strings.Menu.CreateDocuments)
                    }
                    row{
                        simpleButton(ButtonStrings.Back)
                    }
                }
            )
        }
        onText(Strings.Menu.CreateDocuments) {
            //state.override { CollectingDataState(emptyList()) }
            state.override { FillingProvisionOfServicesState.DownloadDocs }
        }
        onText(ButtonStrings.Back) {
            state.override { EmptyState }
        }
    }
}
