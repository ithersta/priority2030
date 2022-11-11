package telegram.entities.state

import kotlinx.serialization.Serializable

object FillingProvisionOfServicesState {
    @Serializable
    object BeginningFillDoc : DialogState

    @Serializable
    object DownloadDocs : DialogState
}