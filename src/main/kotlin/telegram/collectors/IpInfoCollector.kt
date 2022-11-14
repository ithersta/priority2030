package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.IpInfo
import parser.Parser
import parser.ParserRusprofile
import telegram.entities.state.IpCollectorState
import telegram.resources.strings.CollectorStrings
import validation.IsInnValidForIp
import validation.IsOgrnipValidForIp
import validation.IsPhoneNumberValid

fun CollectorMapBuilder.IpInfoCollector() {
    collector<IpInfo>(initialState = IpCollectorState.WaitingForInn) {
        val parser = Parser()
        val parserForData = ParserRusprofile()
        state<IpCollectorState.WaitingForInn> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.inn) }
            onText {
                if (IsInnValidForIp(it.content.text)) {
                    if (parser.parsing(it.content.text) != 200) {
                        state.override { IpCollectorState.HandsWaitingOgrn(it.content.text) }
                    } else {
                        state.override { IpCollectorState.WaitingInspection(it.content.text, parser.fullNameOfOrg) }
                    }
                } else {
                    return@onText
                }
            }
        }
        state<IpCollectorState.WaitingInspection> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.isRight(state.snapshot.fullNameOfOrg)) }
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
                            parser.ogrnOfOrg,
                            parser.fullNameOfHolder,
                            parserForData.parseWebPage(parser.ogrnOfOrg)
                        )
                    }
                } else {
                    state.override { IpCollectorState.WaitingForInn }
                }
            }
        }
        state<IpCollectorState.HandsWaitingOgrn> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.ogrn) }
            onText {
                if (IsOgrnipValidForIp(it.content.text)) {
                    state.override { IpCollectorState.HandsWaitingDataOfOgrn(state.snapshot.inn, it.content.text) }
                } else {
                    return@onText
                }
            }
        }
        state<IpCollectorState.HandsWaitingDataOfOgrn> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.data) }
            onText {
//              TODO: ВАЛИДАЦИЯ для ДАТЫ (ЗАДАНИЕ ВИКИ , не все сделала!)
                state.override {
                    IpCollectorState.HandsWaitingFullNameOfOrg(
                        state.snapshot.inn, state.snapshot.ogrn,
                        it.content.text
                    )
                }
            }
        }
        state<IpCollectorState.HandsWaitingFullNameOfOrg> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.fullName) }
            onText {
//              TODO: Валидация для ИП: ИП ФАМЛИЛИЯ ИМЯ ОТЧЕСТВО
                state.override {
                    IpCollectorState.WaitingPhone(
                        state.snapshot.inn, state.snapshot.ogrn,
                        state.snapshot.dataOgrn, it.content.text
                    )
                }
            }
        }
        state<IpCollectorState.WaitingPhone> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.phone) }
            onText {
                if (IsPhoneNumberValid(it.content.text)) {
                    state.override {
                        IpCollectorState.WaitingEmail(
                            state.snapshot.inn,
                            state.snapshot.ogrn,
                            state.snapshot.fullNameOfOrg,
                            state.snapshot.dataOgrn,
                            it.content.text
                        )
                    }
                } else {
                    return@onText
                }
            }
        }
        state<IpCollectorState.WaitingEmail> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.email) }
            onText {
//              TODO: где валидация для e-mail?
                val info = IpInfo(
                    inn = state.snapshot.inn,
                    ogrn = state.snapshot.ogrn,
                    fullNameIp = state.snapshot.fullNameOfOrg,
                    orgrnData = state.snapshot.dataOgrn,
                    phone = state.snapshot.phone,
                    email = it.content.text
                )
                this@collector.exit(state, listOf(info))
            }
        }
    }
}
