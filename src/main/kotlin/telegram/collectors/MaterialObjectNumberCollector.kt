package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.MaterialObjectNumber
import telegram.entities.state.MaterialObjectNumberState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings

fun CollectorMapBuilder.materialObjectNumberCollector() {
    collector<MaterialObjectNumber>(initialState = MaterialObjectNumberState) {
        state<MaterialObjectNumberState> {
            onEnter { sendTextMessage(it, CollectorStrings.MaterialObjectNumber) }
            onText {
                val materialObjectNumber = it.content.text.toIntOrNull()
                if (materialObjectNumber != null) {
                    val materialObj = MaterialObjectNumber(materialObjectNumber)
                    this@collector.exit(state, listOf(materialObj))
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.InvalidNumber)
                }
            }
        }
    }
}
