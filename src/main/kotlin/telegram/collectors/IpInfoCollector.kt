package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.IpInfo
import parser.Parser
import telegram.entities.state.IpCollectorState
import telegram.resources.strings.CollectorStrings

fun CollectorMapBuilder.IpInfoCollector() {
    collector<IpInfo>(initialState = IpCollectorState.WaitingForInn) {
        state<IpCollectorState.WaitingForInn> {
            val parser = Parser()
            onEnter { sendTextMessage(it, CollectorStrings.IP.inn) }
            onText {
                if (parser.parsing(it.content.text) != 200) {
                    state.override { IpCollectorState.HandsWaitingOgrn(it.content.text) }
                }
            }
            //TODO: переделать все что ниже тк не запускается программа!
//            onEnter { sendTextMessage(it, CollectorStrings.IP.isRight) }
            onEnter { sendTextMessage(it, parser.fullNameOfOrg) }
            onText { message ->
                val response = when (message.content.text) {
                    CollectorStrings.IP.Yes -> "Да"
                    CollectorStrings.IP.No -> "Нет"
                    else -> {
                        sendTextMessage(message.chat, CollectorStrings.IP.Invalid)
                        return@onText
                    }
                }
                if (response.equals("Да")) {
                    state.override {
                        IpCollectorState.WaitingPhone(
                            parser.innOfOrg,
                            parser.orgnOfOrg,
                            parser.okpoOfOrg,
                            parser.fullName
                        )
                    }
                } else {
                    state.override { IpCollectorState.WaitingForInn }
                }
            }
        }
        state<IpCollectorState.WaitingPhone> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.phone) }
            onText {
                state.override {
                    IpCollectorState.WaitingEmail(
                        state.snapshot.inn,
                        state.snapshot.orgn,
                        state.snapshot.okpo,
                        state.snapshot.fullNameOfOrg, it.content.text
                    )
                }
            }
        }
        state<IpCollectorState.WaitingEmail> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.email) }
            onText {
                val info = IpInfo(
                    inn = state.snapshot.inn,
                    orgn = state.snapshot.orgn,
                    okpo = state.snapshot.okpo,
                    fullNameIp = state.snapshot.fullNameOfOrg,
                    phone = state.snapshot.phone,
                    email = it.content.text
                )
                this@collector.exit(state, listOf(info))
            }
        }
    }
}
