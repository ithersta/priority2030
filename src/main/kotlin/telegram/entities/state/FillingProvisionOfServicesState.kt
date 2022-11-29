package telegram.entities.state

import dev.inmo.tgbotapi.requests.abstracts.FileId
import kotlinx.serialization.Serializable

object FillingProvisionOfServicesState {
    @Serializable
    object BeginFillDoc : DialogState

    @Serializable
    object DownloadDocs : DialogState

    @Serializable
    object UploadDocs : DialogState

    @Serializable
    object UploadDocsEmail : DialogState

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
    data class UploadDocDraftAgreement (
        val docs: List<FileId>,
        val docName: List<String>
    ): DialogState

    @Serializable
    data class UploadDocsCommercialOffers (
        val docs: List<FileId>,
        val docName: List<String>
    ) : DialogState

    @Serializable
    data class UploadExtraDocs (
        val docs: List<FileId>,
        val docName: List<String>
    ): DialogState

    @Serializable
    data class SendDocs (
        val docs: List<FileId>,
        val docName: List<String>
    ): DialogState

}
