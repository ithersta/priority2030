package telegram.entities.state

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
    object UploadDocOfficialMemo : DialogState

    @Serializable
    object UploadDocDraftAgreement : DialogState

    @Serializable
    object UploadDocsCommercialOffers : DialogState

    @Serializable
    object UploadExtraDocs : DialogState

    @Serializable
    object SendDocs : DialogState

}
