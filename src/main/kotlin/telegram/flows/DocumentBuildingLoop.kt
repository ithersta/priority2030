package telegram.flows

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import dev.inmo.tgbotapi.extensions.api.send.media.sendDocument
import dev.inmo.tgbotapi.extensions.api.send.withUploadDocumentAction
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import dev.inmo.tgbotapi.types.UserId
import documentSet
import domain.documents.DocumentSet
import telegram.Docx
import telegram.collectors
import telegram.entities.state.CollectingDataState
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.entities.state.FillingProvisionOfServicesState

fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.documentBuildingLoop() {
    state<CollectingDataState> {
        val collectors = collectors()
        onEnter { _ ->
            when (val result = documentSet.build(state.snapshot.fieldsData.associateBy { it::class })) {
                is DocumentSet.Result.MissingData -> {
                    state.push(collectors.getValue(result.kClass))
                }

                is DocumentSet.Result.OK -> {
                    state.override { FillingProvisionOfServicesState.DownloadDocs(result.documents) }
                }
            }
        }
    }
}
