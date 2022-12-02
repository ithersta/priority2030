package telegram.entities.state

import dev.inmo.tgbotapi.requests.abstracts.FileId
import domain.documents.Document
import kotlinx.serialization.Serializable

object FillingProvisionOfServicesState {
    @Serializable
    object BeginFillDoc : DialogState

    @Serializable
    class DownloadDocs(
        val documents: List<Document>
    ) : DialogState

    @Serializable
    object UploadDocs : DialogState

    @Serializable
    class UploadDocsEmail(
        val documents: List<Document>
    ) : DialogState

    @Serializable
    object CheckAndUploadDocs : DialogState

    @Serializable
    object UploadDocApplicationForPlacement : DialogState

    @Serializable
    data class UploadDocOfficialMemo(
        val docs: List<FileId>,
        val docName: List<String>
    ) : DialogState

    @Serializable
    data class UploadDocDraftAgreement(
        val docs: List<FileId>,
        val docName: List<String>
    ) : DialogState

    @Serializable
    data class UploadDocsCommercialOffers(
        val docs: List<FileId>,
        val docName: List<String>
    ) : DialogState

    @Serializable
    data class UploadExtraDocs(
        val docs: List<FileId>,
        val docName: List<String>
    ) : DialogState

    @Serializable
    data class SendDocs(
        val docs: List<FileId>,
        val docName: List<String>
    ) : DialogState

}
