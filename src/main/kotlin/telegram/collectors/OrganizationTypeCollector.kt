package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.utils.row
import domain.datatypes.OrganizationType
import telegram.entities.state.OrganizationTypeState
import telegram.resources.strings.CollectorStrings

fun CollectorMapBuilder.organizationTypeCollector() {
    collector<OrganizationType>(initialState = OrganizationTypeState) {
        state<OrganizationTypeState> {
            onEnter {
                sendTextMessage(it, CollectorStrings.OrganizationType.message,
                    replyMarkup = replyKeyboard(
                        resizeKeyboard = true,
                        oneTimeKeyboard = true
                    ) {
                        row {
                            simpleButton(CollectorStrings.OrganizationType.ip)
                            simpleButton(CollectorStrings.OrganizationType.ooo)
                        }
                    }
                )
            }
            onText { message ->
                val type = when (message.content.text) {
                    CollectorStrings.OrganizationType.ip -> OrganizationType.IP
                    CollectorStrings.OrganizationType.ooo -> OrganizationType.Ooo
                    else -> {
                        sendTextMessage(message.chat, CollectorStrings.OrganizationType.invalid)
                        return@onText
                    }
                }
                this@collector.exit(state, listOf(type))
            }
        }
    }
}
