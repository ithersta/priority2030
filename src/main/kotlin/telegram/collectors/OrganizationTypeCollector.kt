package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.OrganizationType
import telegram.entities.state.OrganizationTypeState
import telegram.resources.strings.CollectorStrings

fun CollectorMapBuilder.organizationTypeCollector() {
    collector<OrganizationType>(initialState = OrganizationTypeState) {
        state<OrganizationTypeState> {
            onEnter { sendTextMessage(it, CollectorStrings.OrganizationType.Message) }
            onText { message ->
                val type = when (message.content.text) {
                    CollectorStrings.OrganizationType.IP -> OrganizationType.IP
                    CollectorStrings.OrganizationType.Ooo -> OrganizationType.Ooo
                    else -> {
                        sendTextMessage(message.chat, CollectorStrings.OrganizationType.Invalid)
                        return@onText
                    }
                }
                this@collector.exit(state, listOf(type))
            }
        }
    }
}
