package telegram.flows

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.utils.row
import telegram.entities.state.DialogState
import telegram.entities.state.FillingProvisionOfServicesState.BeginningFillDoc
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import telegram.entities.state.EmptyState
import telegram.entities.state.FillingProvisionOfServicesState
import telegram.resources.strings.ButtonStrings.Back
import telegram.resources.strings.Strings.Menu.CreateDocuments

fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.fillDocsProvisionOfServices() {
    state<BeginningFillDoc> {
        onEnter { chatId ->
            sendTextMessage(
                chatId,
                CreateDocuments,
                replyMarkup = replyKeyboard(
                    resizeKeyboard = true,
                    oneTimeKeyboard = true
                ) {
                    row {
                        simpleButton(CreateDocuments)
                        simpleButton(Back)
                    }
                }
            )
        }
        onText(CreateDocuments) {
            //TODO: (Для Саши) тут переход к состоянию заполнению полей документов
            state.override { FillingProvisionOfServicesState.DownloadDocs }
        }
        onText(Back) {
            state.override { EmptyState }
        }
    }
}
