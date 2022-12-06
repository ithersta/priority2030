package telegram.flows

import MainProperties
import com.ithersta.tgbotapi.fsm.StatefulContext
import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onDocument
import com.ithersta.tgbotapi.fsm.entities.triggers.onDocumentMediaGroup
import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.files.downloadFile
import dev.inmo.tgbotapi.extensions.api.send.media.sendDocument
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import dev.inmo.tgbotapi.types.chat.Chat
import dev.inmo.tgbotapi.types.files.DocumentFile
import dev.inmo.tgbotapi.utils.row
import domain.entities.Email
import email.Attachment
import email.EmailSender
import org.koin.core.component.get
import org.koin.core.component.inject
import telegram.Docx
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.entities.state.FillingProvisionOfServicesState
import telegram.entities.state.FillingProvisionOfServicesState.WaitingForDocs
import telegram.entities.state.FillingProvisionOfServicesState.WaitingForDocs.Type
import telegram.entities.state.FillingProvisionOfServicesState.WaitingForDocs.UploadedDocument
import telegram.resources.strings.ButtonStrings
import telegram.resources.strings.EmailStrings
import telegram.resources.strings.InvalidInputStrings
import telegram.resources.strings.Strings

const val MAX_SIZE_OF_DOC = 20971520

fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.afterDocsGenerationFlow() {
    downloadDocsFlow()
    afterDownloadState()
    uploadDocsFlow()
}

private fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.afterDownloadState() {
    state<FillingProvisionOfServicesState.AfterDownload> {
        onEnter { chatId ->
            sendTextMessage(
                chatId,
                Strings.UploadPackageDocs,
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
}

private fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.downloadDocsFlow() {
    val emailSender: EmailSender by inject()
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
        onText(ButtonStrings.CheckingDoc) { message ->
            state.snapshot.documents.forEach {
                sendDocument(message.chat, Docx.load(it).asMultipartFile(it.filename))
            }
            state.override { FillingProvisionOfServicesState.AfterDownload }
        }
        onText(ButtonStrings.GetByEmail) {
            state.override { FillingProvisionOfServicesState.DownloadDocsByEmail(documents) }
        }
    }
    state<FillingProvisionOfServicesState.DownloadDocsByEmail> {
        onEnter { chatId ->
            sendTextMessage(chatId, Strings.Email)
        }
        onText { message ->
            val email = Email.of(message.content.text)
            if (email != null) {
                val attachments = state.snapshot.documents.map { Attachment(Docx.load(it), it.filename, it.filename) }
                emailSender.sendFiles(
                    email,
                    attachments,
                    EmailStrings.ToBotUser.Subject
                )
                sendTextMessage(message.chat, Strings.SuccessfulSendDocsEmail)
                state.override { FillingProvisionOfServicesState.AfterDownload }
            } else {
                sendTextMessage(message.chat, InvalidInputStrings.InvalidEmail)
            }
        }
    }
}

private fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.uploadDocsFlow() {
    checkAndUploadDocsState()
    waitingForDocsState()
    sendDocsState()
}

private fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.checkAndUploadDocsState() {
    state<FillingProvisionOfServicesState.CheckAndUploadDocs> {
        onEnter { chatId ->
            sendTextMessage(
                chatId, Strings.CheckListOfDocs,
                replyMarkup = replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                    row { simpleButton(ButtonStrings.UploadDocuments) }
                }
            )
        }
        onText(ButtonStrings.UploadDocuments) { state.override { WaitingForDocs() } }
    }
}

private fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.waitingForDocsState() {
    state<WaitingForDocs> {
        onEnter { chatId ->
            val type = state.snapshot.type ?: run {
                state.override { FillingProvisionOfServicesState.WaitingForFullNameOfInitiator(docs) }
                return@onEnter
            }
            sendTextMessage(
                chatId,
                when (type) {
                    Type.ApplicationForPlacement -> Strings.UploadDocs.ApplicationForPlacement
                    Type.OfficialMemo -> Strings.UploadDocs.OfficialMemo
                    Type.DraftAgreement -> Strings.UploadDocs.DraftAgreement
                    Type.CommercialOffer -> Strings.UploadDocs.CommercialOffers
                    Type.Extra -> Strings.UploadDocs.ExtraDocs
                },
                replyMarkup = if (type.max != type.min) replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                    row { simpleButton(ButtonStrings.UploadedAllDocs) }
                } else ReplyKeyboardRemove()
            )
        }
        onDocument { handleUploadedDocuments(it.chat, listOf(it.content.media)) }
        onDocumentMediaGroup { message ->
            handleUploadedDocuments(message.chat, message.content.group.map { it.content.media })
        }
        onText(ButtonStrings.UploadedAllDocs) { message ->
            val type = state.snapshot.type ?: return@onText
            val count = state.snapshot.docs.count { type == it.type }
            if (count in type.min..type.max) {
                state.override { copy(typeIndex = typeIndex + 1) }
            } else {
                sendTextMessage(message.chat, Strings.incorrectNumOfDocs(count, type.min))
            }
        }
    }
}

private fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.sendDocsState() {
    state<FillingProvisionOfServicesState.WaitingForFullNameOfInitiator> {
        onEnter { sendTextMessage(it, Strings.InitiatorFullName) }
        onText { state.override { FillingProvisionOfServicesState.SendDocs(it.content.text, docs) } }
    }
    state<FillingProvisionOfServicesState.SendDocs> {
        onEnter { chatId ->
            sendTextMessage(
                chatId, Strings.SendDocuments,
                replyMarkup = replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                    row {
                        simpleButton(ButtonStrings.No)
                        simpleButton(ButtonStrings.Send)
                    }
                }
            )
        }
        val emailSender: EmailSender by inject()
        val emailTo = get<MainProperties>().emailTo
        onText(ButtonStrings.Send) { message ->
            val attachments = state.snapshot.docs.map { Attachment(downloadFile(it.fileId), it.filename, it.filename) }
            emailSender.sendFiles(
                to = emailTo,
                attachments = attachments,
                subject = EmailStrings.ToAdmin.subject(state.snapshot.initiatorFullName)
            )
            sendTextMessage(message.chat, Strings.SuccessfulSendDocs)
            state.override { EmptyState }
        }
        onText(ButtonStrings.No) { state.override { EmptyState } }
    }
}

private suspend fun StatefulContext<DialogState, *, WaitingForDocs, *>.handleUploadedDocuments(
    chat: Chat,
    group: List<DocumentFile>
) {
    val type = state.snapshot.type ?: return
    val oldCount = state.snapshot.docs.count { it.type == type }
    val newDocs = state.snapshot.docs + group
        .filter { document ->
            val fileSize = document.fileSize
            (fileSize != null && fileSize < MAX_SIZE_OF_DOC).also {
                if (!it) {
                    sendTextMessage(chat, Strings.tooBigFileSize(document.fileName.orEmpty()))
                }
            }
        }
        .take(type.max - oldCount)
        .map { UploadedDocument(it.fileId, it.fileName.orEmpty(), type) }
    val count = newDocs.count { it.type == type }
    when {
        count == type.max -> state.override { copy(docs = newDocs, typeIndex = typeIndex + 1) }
        count > type.max -> error("File count is bigger than the max value")
        else -> state.overrideQuietly { copy(docs = newDocs) }
    }
}
