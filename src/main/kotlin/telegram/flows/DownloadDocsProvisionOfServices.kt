package telegram.flows

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onDocument
import com.ithersta.tgbotapi.fsm.entities.triggers.onDocumentMediaGroup
import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.files.downloadFile
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.utils.row
import domain.entities.Email
import email.Attachment
import email.EmailSender
import org.koin.core.component.inject
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.entities.state.FillingProvisionOfServicesState
import telegram.resources.strings.ButtonStrings
import telegram.resources.strings.InvalidInputStrings
import telegram.resources.strings.Strings

const val MIN_NUM_OF_COMMERCIAL_OFFERS = 3
const val NUM_OF_PREVIOUS_DOCS = 3
const val MAX_SIZE_OF_DOC = 20971520
//(для Глеба) тут нужно состояние, которое будет хранить заполненные документы
//из последнего состояния заполнения переход сюда
fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.downloadDocsProvisionOfServices() {
    state<FillingProvisionOfServicesState.DownloadDocs> {
        onEnter { chatId ->
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
                    row {
                        simpleButton(ButtonStrings.GetByEmail)
                    }
                }
            )
        }
        onText(ButtonStrings.CheckingDoc) {
            //отправка доков в чат
            state.override { FillingProvisionOfServicesState.UploadDocs }
        }
        onText(ButtonStrings.GetByEmail) {
            state.override { FillingProvisionOfServicesState.UploadDocsEmail }
        }
    }
    //тут тоже в состоянии должны храниться документы
    state<FillingProvisionOfServicesState.UploadDocsEmail> {
        onEnter { chatId ->
            sendTextMessage(
                chatId,
                Strings.Email
            )
        }
        onText { message ->
            val email = Email.of(message.content.text)
            if (email != null) {
                sendTextMessage(message.chat, Strings.SuccessfulSendDocsEmail)
                state.override { FillingProvisionOfServicesState.UploadDocs }
            } else {
                sendTextMessage(message.chat, InvalidInputStrings.InvalidEmail)
            }
        }
    }
    state<FillingProvisionOfServicesState.UploadDocs> {
        onEnter { chatId ->
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
                    row {
                        simpleButton(ButtonStrings.BackToCreateDocs)
                    }
                }
            )
        }
        onText(ButtonStrings.UploadPackageDoc) {
            state.override { FillingProvisionOfServicesState.CheckAndUploadDocs }
        }
        onText(ButtonStrings.BackToCreateDocs) {
            state.override { EmptyState }
        }
    }
    state<FillingProvisionOfServicesState.CheckAndUploadDocs> {
        onEnter { chatId ->
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
        onText(ButtonStrings.UploadDocuments) {
            state.override { FillingProvisionOfServicesState.UploadDocApplicationForPlacement }
        }
    }
    state<FillingProvisionOfServicesState.UploadDocApplicationForPlacement> {
        onEnter { chatId ->
            sendTextMessage(
                chatId,
                Strings.UploadDocs.ApplicationForPlacement
            )
        }
        onDocument { message ->
            if (message.content.media.fileSize!! < MAX_SIZE_OF_DOC) {
                state.override {
                    FillingProvisionOfServicesState.UploadDocOfficialMemo(
                        docs = listOf(message.content.media.fileId),
                        docName = listOf(message.content.media.fileName.toString())
                    )
                }
            } else {
                sendTextMessage(message.chat, Strings.TooBigFileSize)
            }
        }
    }
    state<FillingProvisionOfServicesState.UploadDocOfficialMemo> {
        onEnter { chatId ->
            sendTextMessage(
                chatId,
                Strings.UploadDocs.OfficialMemo
            )
        }
        onDocument { message ->
            if (message.content.media.fileSize!! < MAX_SIZE_OF_DOC) {
                state.override {
                    FillingProvisionOfServicesState.UploadDocDraftAgreement(
                        this.docs + message.content.media.fileId,
                        this.docName + message.content.media.fileName.toString()
                    )
                }
            } else {
                sendTextMessage(message.chat, Strings.TooBigFileSize)
            }
        }
    }
    state<FillingProvisionOfServicesState.UploadDocDraftAgreement> {
        onEnter { chatId ->
            sendTextMessage(
                chatId,
                Strings.UploadDocs.DraftAgreement
            )
        }
        onDocument { message ->
            if (message.content.media.fileSize!! < MAX_SIZE_OF_DOC) {
                state.override {
                    FillingProvisionOfServicesState.UploadDocsCommercialOffers(
                        this.docs + message.content.media.fileId,
                        this.docName + message.content.media.fileName.toString()
                    )
                }
            } else {
                sendTextMessage(message.chat, Strings.TooBigFileSize)
            }
        }
    }
    state<FillingProvisionOfServicesState.UploadDocsCommercialOffers> {
        onEnter { chatId ->
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
        onDocumentMediaGroup { message ->
            //добавить проверку тут
            state.overrideQuietly {
                copy(docs = docs + message.content.group.map { it.content.media.fileId },
                    docName = docName + message.content.group.map { it.content.media.fileName.toString() })
            }
        }
        onDocument { message ->
            if (message.content.media.fileSize!! < MAX_SIZE_OF_DOC) {
                state.overrideQuietly {
                    copy(
                        docs = docs + message.content.media.fileId,
                        docName = docName + message.content.media.fileName.toString()
                    )
                }
            } else {
                sendTextMessage(message.chat, Strings.TooBigFileSize)
            }
        }
        onText(ButtonStrings.UploadadAllDocs) {
            if ((state.snapshot.docs.size - NUM_OF_PREVIOUS_DOCS) < MIN_NUM_OF_COMMERCIAL_OFFERS) {
                sendTextMessage(
                    it.chat.id,
                    Strings.IncorrectNumOfDocs
                )
                state.override { FillingProvisionOfServicesState.UploadDocsCommercialOffers(this.docs, this.docName) }
            } else
                state.override { FillingProvisionOfServicesState.UploadExtraDocs(this.docs, this.docName) }
        }
    }
    state<FillingProvisionOfServicesState.UploadExtraDocs> {
        onEnter { chatId ->
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
        onText(ButtonStrings.NotRequired) {
            state.override { FillingProvisionOfServicesState.SendDocs(this.docs, this.docName) }
        }
        onDocumentMediaGroup { message ->
           //добавить проверку тут
            state.overrideQuietly {
                copy(docs = docs + message.content.group.map { it.content.media.fileId },
                    docName = docName + message.content.group.map { it.content.media.fileName.toString() })
            }
        }
        onDocument { message ->
            if (message.content.media.fileSize!! < MAX_SIZE_OF_DOC) {
                state.overrideQuietly {
                    copy(
                        docs = docs + message.content.media.fileId,
                        docName = docName + message.content.media.fileName.toString()
                    )
                }
            } else {
                sendTextMessage(message.chat, Strings.TooBigFileSize)
            }
        }
        onText(ButtonStrings.UploadadAllDocs) {
            state.override { FillingProvisionOfServicesState.SendDocs(this.docs, this.docName) }
        }
    }
    state<FillingProvisionOfServicesState.SendDocs> {
        onEnter { chatId ->
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
        val emailSender: EmailSender by inject()
        onText(ButtonStrings.Send) { message ->
            val attachments = state.snapshot.docs.zip(state.snapshot.docName)
                .map { Attachment(downloadFile(it.first), it.second, "описание") }
            emailSender.sendFiles(email.Strings.Email, attachments)
            sendTextMessage(message.chat, Strings.SuccessfulSendDocs)
            state.override { EmptyState }
        }
    }
}
