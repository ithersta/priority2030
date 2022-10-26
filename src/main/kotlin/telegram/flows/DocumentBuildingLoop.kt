package telegram.flows

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.UserId
import documentSet
import domain.documents.DocumentSet
import telegram.collectors
import telegram.entities.state.CollectingDataState
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState

fun RoleFilterBuilder<DialogState, Unit, Unit, UserId>.documentBuildingLoop() {
    state<CollectingDataState> {
        val collectors = collectors()
        onEnter { chatId ->
            when (val result = documentSet.build(state.snapshot.fieldsData.associateBy { it::class })) {
                is DocumentSet.Result.MissingData -> {
                    state.push(collectors.getValue(result.kClass))
                }

                is DocumentSet.Result.OK -> {
                    sendTextMessage(chatId, "Документы построены")
                    state.override { EmptyState }
                }
            }
        }
    }
}
