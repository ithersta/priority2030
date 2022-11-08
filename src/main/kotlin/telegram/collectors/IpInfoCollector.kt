package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.IpInfo
import telegram.entities.state.IpCollectorState
import telegram.resources.strings.CollectorStrings

fun CollectorMapBuilder.IpInfoCollector() {
    collector<IpInfo>(initialState = IpCollectorState.WaitingForInn) {
        state<IpCollectorState.WaitingForInn> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.inn) }
            onText {
                state.override { IpCollectorState.WaitingForInfo(it.content.text) }
            }
        }
    }
}
