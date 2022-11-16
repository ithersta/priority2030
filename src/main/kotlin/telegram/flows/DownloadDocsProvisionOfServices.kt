package telegram.flows

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onDocument
import com.ithersta.tgbotapi.fsm.entities.triggers.onDocumentMediaGroup
import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.requests.abstracts.FileId
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.utils.row
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.entities.state.FillingProvisionOfServicesState
import telegram.resources.strings.ButtonStrings
import telegram.resources.strings.Strings

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
            //отправка доков в чат
            state.override { FillingProvisionOfServicesState.UploadDocs }
        }
    }
    state<FillingProvisionOfServicesState.UploadDocs>{
        onEnter{chatId->
            sendTextMessage(
                chatId,
                ButtonStrings.UploadPackageDoc,
                replyMarkup = replyKeyboard(
                    resizeKeyboard = true,
                    oneTimeKeyboard = true
                ) {
                    row {
                        simpleButton(ButtonStrings.UploadPackageDoc)
                        simpleButton(ButtonStrings.BackToCreateDocs)
                    }
                }
            )
        }
        onText(ButtonStrings.UploadPackageDoc){
            state.override { FillingProvisionOfServicesState.CheckAndUploadDocs }
        }
        onText(ButtonStrings.BackToCreateDocs){
            state.override { EmptyState }
        }
    }
    state<FillingProvisionOfServicesState.CheckAndUploadDocs>{
        onEnter{chatId->
            sendTextMessage(
                chatId,
                Strings.checkingListOfDocs(),
                replyMarkup = replyKeyboard(
                    resizeKeyboard = true,
                    oneTimeKeyboard = true
                ) {
                    row {
                        simpleButton(ButtonStrings.UploadDocuments)
                    }
                }
            )
        }
        onText(ButtonStrings.UploadDocuments){
            state.override { FillingProvisionOfServicesState.UploadDocApplicationForPlacement }
        }
    }
    state<FillingProvisionOfServicesState.UploadDocApplicationForPlacement> {
        onEnter{chatId->
            sendTextMessage(
                chatId,
                Strings.UploadDocs.ApplicationForPlacement
            )
        }
        onDocument{message->
//            val x = message.content.media
//            val pp: ArrayList<FileId> = ArrayList()
//            pp.add(x.fileId)
//            x.forEach { y->
//                pp.add(y.content.media.fileId)
//            }
            state.override {FillingProvisionOfServicesState.UploadDocOfficialMemo(docs = listOf(message.content.media.fileId))}
        }
    }
    state<FillingProvisionOfServicesState.UploadDocOfficialMemo> {
        onEnter{chatId->
            sendTextMessage(
                chatId,
                Strings.UploadDocs.OfficialMemo
            )
        }
        onDocument{ message->
            state.override { FillingProvisionOfServicesState.UploadDocDraftAgreement(this.docs + message.content.media.fileId) }
        }
    }
    state<FillingProvisionOfServicesState.UploadDocDraftAgreement> {
        onEnter{chatId->
            sendTextMessage(
                chatId,
                Strings.UploadDocs.DraftAgreement
            )
        }
        onDocument{ message->
            state.override { FillingProvisionOfServicesState.UploadDocsCommercialOffers(this.docs + message.content.media.fileId) }
        }
    }
    state<FillingProvisionOfServicesState.UploadDocsCommercialOffers> {
        onEnter{chatId->
            sendTextMessage(
                chatId,
                Strings.UploadDocs.CommercialOffers
            )
        }
        onDocumentMediaGroup{ message->
            state.override { FillingProvisionOfServicesState.UploadExtraDocs(this.docs + message.content.media.fileId) }
        }
    }
    state<FillingProvisionOfServicesState.UploadExtraDocs> {
        onEnter{chatId->
            sendTextMessage(
                chatId,
                Strings.UploadDocs.ExtraDocs,
                replyMarkup = replyKeyboard(
                    resizeKeyboard = true,
                    oneTimeKeyboard = true
                ) {
                    row {
                        simpleButton(ButtonStrings.NotRequired)
                    }
                }
            )
        }
        onText(ButtonStrings.NotRequired){
            state.override { FillingProvisionOfServicesState.SendDocs(this.docs) }
        }
        onDocumentMediaGroup{ message->
            state.override { FillingProvisionOfServicesState.SendDocs(this.docs + message.content.media.fileId) }
        }
    }
    state<FillingProvisionOfServicesState.SendDocs> {
        onEnter{chatId->
            sendTextMessage(
                chatId,
                Strings.SendDocuments,
                replyMarkup = replyKeyboard(
                    resizeKeyboard = true,
                    oneTimeKeyboard = true
                ) {
                    row {
                        simpleButton(ButtonStrings.Send)
                    }
                }
            )
        }
        onText(ButtonStrings.Send){
            state.override { EmptyState }
        }
    }
}
