package telegram.flows

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onDocument
import com.ithersta.tgbotapi.fsm.entities.triggers.onDocumentMediaGroup
import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.utils.row
import domain.entities.Email
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.entities.state.FillingProvisionOfServicesState
import telegram.resources.strings.ButtonStrings
import telegram.resources.strings.InvalidInputStrings
import telegram.resources.strings.Strings

const val MIN_NUM_OF_COMMERCIAL_OFFERS = 3
const val NUM_OF_PREVIOUS_DOCS = 3
//(для Глеба) тут нужно состояние, которое будет хранить заполненные документы
//из последнего состояния заполнения переход сюда
fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.downloadDocsProvisionOfServices() {
    state<FillingProvisionOfServicesState.DownloadDocs>{
        onEnter{chatId->
            sendTextMessage(
                chatId,
                Strings.PackageDocsReady,
                replyMarkup = replyKeyboard(
                    resizeKeyboard = true,
                    oneTimeKeyboard = true
                ) {
                    row {
                        simpleButton(ButtonStrings.CheckingDoc)
                    }
                    row{
                        simpleButton(ButtonStrings.GetByEmail)
                    }
                }
            )
        }
        onText(ButtonStrings.CheckingDoc){
            //отправка доков в чат
            state.override { FillingProvisionOfServicesState.UploadDocs }
        }
        onText(ButtonStrings.GetByEmail){
            state.override{ FillingProvisionOfServicesState.UploadDocsEmail }
        }
    }
    //тут тоже в состоянии должны храниться документы
    state<FillingProvisionOfServicesState.UploadDocsEmail>{
        onEnter{chatId->
            sendTextMessage(
                chatId,
                Strings.Email
            )
        }
        onText{message->
            val email = Email.of(message.content.text)
            if(email != null) {
                //отправка на введенный адрес
                sendTextMessage(message.chat, Strings.SuccessfulSendDocsEmail)
                state.override { FillingProvisionOfServicesState.UploadDocs }
            }
            else {
                sendTextMessage(message.chat, InvalidInputStrings.InvalidEmail)
            }
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
                    }
                    row{
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
            state.override {
                FillingProvisionOfServicesState.UploadDocOfficialMemo(docs = listOf(message.content.media.fileId))
            }
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
            state.override {
                FillingProvisionOfServicesState.UploadDocDraftAgreement(this.docs + message.content.media.fileId)
            }
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
            state.override {
                FillingProvisionOfServicesState.UploadDocsCommercialOffers(this.docs + message.content.media.fileId)
            }
        }
    }
    state<FillingProvisionOfServicesState.UploadDocsCommercialOffers> {
        onEnter{chatId->
            sendTextMessage(
                chatId,
                Strings.UploadDocs.CommercialOffers,
                replyMarkup = replyKeyboard(
                    resizeKeyboard = true,
                    oneTimeKeyboard = true
                ) {
                    row {
                        simpleButton(ButtonStrings.UploadadAllDocs)
                    }
                }
            )
        }
        onDocumentMediaGroup{ message->
            state.overrideQuietly {copy(docs = docs + message.content.group.map { it.content.media.fileId })
            }
        }
        onDocument{message->
            state.overrideQuietly { copy(docs = docs + message.content.media.fileId) }
        }
        onText(ButtonStrings.UploadadAllDocs){
            if(( state.snapshot.docs.size - NUM_OF_PREVIOUS_DOCS) < MIN_NUM_OF_COMMERCIAL_OFFERS){
                sendTextMessage(
                    it.chat.id,
                    Strings.IncorrectNumOfDocs
                )
                state.override { FillingProvisionOfServicesState.UploadDocsCommercialOffers(this.docs) }
            } else
            state.override { FillingProvisionOfServicesState.UploadExtraDocs(this.docs) }
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
                    row {
                        simpleButton(ButtonStrings.UploadadAllDocs)
                    }
                }
            )
        }
        onText(ButtonStrings.NotRequired){
            state.override { FillingProvisionOfServicesState.SendDocs(this.docs) }
        }
        onDocumentMediaGroup{ message->
            state.overrideQuietly { copy(docs = docs + message.content.group.map { it.content.media.fileId }) }
        }
        onDocument{ message->
            state.overrideQuietly { copy(docs = docs + message.content.media.fileId) }
        }
        onText(ButtonStrings.UploadadAllDocs){
            state.override { FillingProvisionOfServicesState.SendDocs(this.docs) }
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
        onText(ButtonStrings.Send){message->
            //тут отправка всех документов на почту Тамары
            sendTextMessage(message.chat, Strings.SuccessfulSendDocs)
            state.override { EmptyState }
        }
    }
}
