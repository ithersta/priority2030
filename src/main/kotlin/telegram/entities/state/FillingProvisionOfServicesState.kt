package telegram.entities.state

import dev.inmo.tgbotapi.types.files.DocumentFile
import dev.inmo.tgbotapi.types.message.content.DocumentMediaGroupPartContent
import dev.inmo.tgbotapi.types.message.content.MediaGroupCollectionContent
import kotlinx.serialization.Serializable

object FillingProvisionOfServicesState {
    @Serializable
    object BeginFillDoc : DialogState

    @Serializable
    object DownloadDocs : DialogState

    @Serializable
    object UploadDocs : DialogState

    @Serializable
    object CheckAndUploadDocs : DialogState

    @Serializable
    object UploadDocApplicationForPlacement : DialogState

    @Serializable
    data class UploadDocOfficialMemo(
        val docs: List<MediaGroupCollectionContent.PartWrapper<DocumentMediaGroupPartContent>>
    ) : DialogState

    @Serializable
    data class UploadDocDraftAgreement (
        val docs: List<MediaGroupCollectionContent.PartWrapper<DocumentMediaGroupPartContent>>
    ): DialogState

    @Serializable
    data class UploadDocsCommercialOffers (
        val docs: List<MediaGroupCollectionContent.PartWrapper<DocumentMediaGroupPartContent>>
    ) : DialogState

    @Serializable
    data class UploadExtraDocs (
        val docs: List<MediaGroupCollectionContent.PartWrapper<DocumentMediaGroupPartContent>>
    ): DialogState

    @Serializable
    data class SendDocs (
        val docs: List<MediaGroupCollectionContent.PartWrapper<DocumentMediaGroupPartContent>>
    ): DialogState

}
