package telegram.entities.state

import dev.inmo.tgbotapi.requests.abstracts.FileId
import domain.documents.Document
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
    object AfterDownload : DialogState

    @Serializable
    class DownloadDocsByEmail(
        val documents: List<Document>
    ) : DialogState

    @Serializable
    object CheckAndUploadDocs : DialogState

    @Serializable
    data class WaitingForDocs(
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
        val docs: List<WaitingForDocs.UploadedDocument>
    ) : DialogState

    @Serializable
    data class WaitingForEmail(
        val initiatorFullName: String,
        val docs: List<WaitingForDocs.UploadedDocument>
    ) : DialogState

    @Serializable
    data class SendDocs(
        val initiatorFullName: String,
        val replyEmail: String,
        val docs: List<WaitingForDocs.UploadedDocument>
    ) : DialogState
}
