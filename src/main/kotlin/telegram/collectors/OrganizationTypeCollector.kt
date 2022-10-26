package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.OrganizationType
import telegram.entities.state.OrganizationTypeState

fun CollectorMapBuilder.organizationTypeCollector() {
    collector<OrganizationType>(initialState = OrganizationTypeState) {
        state<OrganizationTypeState> {
            onEnter { sendTextMessage(it, "Введите тип организации") }
            onText { message ->
                val type = when (message.content.text) {
                    "ИП" -> OrganizationType.IP
                    "OOO" -> OrganizationType.Ooo
                    else -> {
                        sendTextMessage(message.chat, "Доступные варианты: ИП, ООО")
                        return@onText
                    }
                }
                this@collector.exit(state, listOf(type))
            }
        }
    }
}
