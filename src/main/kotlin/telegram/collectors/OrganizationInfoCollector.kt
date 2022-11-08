package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.CompanyInfo
import telegram.entities.state.CompanyCollectorState
import telegram.resources.strings.CollectorStrings

fun CollectorMapBuilder.organizationInfoCollector() {
    collector<CompanyInfo>(initialState = CompanyCollectorState.WaitingForInn) {
        state<CompanyCollectorState.WaitingForInn> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.inn) }
            onText {
                state.override { CompanyCollectorState.WaitingForKpp(it.content.text) }
            }
        }
    }
}
