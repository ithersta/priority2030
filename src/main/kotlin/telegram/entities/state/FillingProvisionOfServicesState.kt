package telegram.entities.state

import kotlinx.serialization.Serializable

object FillingProvisionOfServicesState {
    @Serializable
    object BeginningFillDoc : DialogState

    @Serializable
    object DownloadingDocs : DialogState

    @Serializable
    object UploadingDocs : DialogState

    @Serializable
    object CheckingAndUploadingDocs : DialogState

}
