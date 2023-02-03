package telegram.entities.state

import dev.inmo.tgbotapi.requests.abstracts.FileId
import domain.documents.Document
import domain.entities.Email
import kotlinx.serialization.Serializable

const val MIN_NUM_OF_COMMERCIAL_OFFERS = 3

object FillingProvisionOfServicesState {
    @Serializable
    object BeginFillDoc : DialogState

    @Serializable
    class DownloadDocs(
        val documents: List<Document>
    ) : DialogState

    @Serializable
    data class AfterDownload(
        val replyTo: Email?
    ) : DialogState

    @Serializable
    class DownloadDocsByEmail(
        val documents: List<Document>
    ) : DialogState

    @Serializable
    class CheckAndUploadDocs(
        val replyTo: Email?
    ) : DialogState

    @Serializable
    data class WaitingForDocs(
        val replyTo: Email?,
        val docs: List<UploadedDocument> = emptyList(),
        val typeIndex: Int = 0
    ) : DialogState {
        val type = Type.values().getOrNull(typeIndex)

        enum class Type(val min: Int, val max: Int) {
            ApplicationForPlacement(1, 1),
            OfficialMemo(1, 1),
            DraftAgreement(1, 1),
            CommercialOffer(MIN_NUM_OF_COMMERCIAL_OFFERS, Int.MAX_VALUE),
            Extra(0, Int.MAX_VALUE)
        }

        @Serializable
        class UploadedDocument(
            val fileId: FileId,
            val filename: String,
            val type: Type
        )
    }

    @Serializable
    data class WaitingForFullNameOfInitiator(
        val replyTo: Email?,
        val docs: List<WaitingForDocs.UploadedDocument>
    ) : DialogState

    @Serializable
    data class WaitingForReplyToEmail(
        val initiatorFullName: String,
        val docs: List<WaitingForDocs.UploadedDocument>
    ) : DialogState

    @Serializable
    data class ConfirmReplyToEmail(
        val initiatorFullName: String,
        val replyTo: Email,
        val docs: List<WaitingForDocs.UploadedDocument>
    ) : DialogState

    @Serializable
    data class SendDocs(
        val initiatorFullName: String,
        val replyTo: Email,
        val docs: List<WaitingForDocs.UploadedDocument>
    ) : DialogState
}
