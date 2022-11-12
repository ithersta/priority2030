package telegram.flows

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.utils.row
import telegram.entities.state.DialogState
import telegram.entities.state.FillingProvisionOfServicesState
import telegram.resources.strings.ButtonStrings

fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.downloadDocsProvisionOfServices() {
    state<FillingProvisionOfServicesState.DownloadDocs>{
        onEnter{chatId->
            sendTextMessage(
                chatId,
                ButtonStrings.CheckingDoc,
                replyMarkup = replyKeyboard(
                    resizeKeyboard = true,
                    oneTimeKeyboard = true
                ) {
                    row {
                        simpleButton(ButtonStrings.CheckingDoc)
                    }
                }
            )
        }
        onText(ButtonStrings.CheckingDoc){
            //TODO: отправка доков в чат
            state.override { FillingProvisionOfServicesState.UploadDocs }
        }
    }
}
