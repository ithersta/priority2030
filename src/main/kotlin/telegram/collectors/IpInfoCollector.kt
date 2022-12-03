package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.utils.row
import domain.datatypes.EntrepreneurInformation
import domain.datatypes.IpInfo
import domain.entities.Email
import parser.Parser
import telegram.entities.state.IpCollectorState
import telegram.resources.strings.CollectorStrings
import validation.*


fun CollectorMapBuilder.ipInfoCollector() {
    collector<EntrepreneurInformation>(initialState = IpCollectorState.WaitingForInn) {
        val parser = Parser()
        state<IpCollectorState.WaitingForInn> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.Inn) }
            onText { message ->
                if (IsInnValidForIp(message.content.text)) {
                    val mainInfo = parser.parsing(message.content.text)
                    if (mainInfo != null) {
                        if (mainInfo.inn == "0") {
                            sendTextMessage(message.chat, CollectorStrings.Recommendations.IsWrongIp)
                            sendTextMessage(message.chat, CollectorStrings.Recommendations.InnForIp)
                            return@onText
                        }
                        state.override { IpCollectorState.WaitingInspection(mainInfo, mainInfo.fullNameOfHolder) }
                    } else {
                        state.override { IpCollectorState.HandsWaitingOgrn(message.content.text) }
                    }
                } else {
                    sendTextMessage(message.chat, CollectorStrings.Recommendations.InnForIp)
                    return@onText
                }
            }
        }
        state<IpCollectorState.WaitingInspection> {
            onEnter {
                sendTextMessage(it,
                    CollectorStrings.IP.isRight(state.snapshot.fullNameOfHolder),
                    replyMarkup = replyKeyboard(
                        resizeKeyboard = true, oneTimeKeyboard = true
                    ) {
                        row {
                            simpleButton(CollectorStrings.IP.Yes)
                        }
                        row {
                            simpleButton(CollectorStrings.IP.No)
                        }
                    })
            }
            onText { message ->
                when (message.content.text) {
                    CollectorStrings.IP.Yes -> {
                        state.override { IpCollectorState.WaitingPhone(mainInfo) }
                    }
                    CollectorStrings.IP.No -> {
                        state.override { IpCollectorState.WaitingForInn }
                    }
                    else -> {
                        sendTextMessage(message.chat, CollectorStrings.IP.Invalid)
                        return@onText
                    }
                }
            }
        }
        state<IpCollectorState.HandsWaitingOgrn> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.Ogrn) }
            onText {
                if (IsOgrnipValidForIp(it.content.text)) {
                    state.override { IpCollectorState.HandsWaitingDataOfOgrn(this.inn, it.content.text) }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.OgrnForIp)
                    return@onText
                }
            }
        }
        state<IpCollectorState.HandsWaitingDataOfOgrn> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.Data) }
            onText {
                state.override { IpCollectorState.HandsWaitingfullNameOfHolder(this.inn, this.ogrn, it.content.text) }
            }
        }
        state<IpCollectorState.HandsWaitingfullNameOfHolder> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.FullName) }
            onText {
                if (IsFullNameValid(it.content.text)) {
                    state.override {
                        IpCollectorState.HandsWaitingLocation(this.inn, this.ogrn, this.dataOgrn, it.content.text)
                    }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.FullName)
                    return@onText
                }
            }
        }
        state<IpCollectorState.HandsWaitingLocation> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.Location) }
            onText {
                state.override {
                    IpCollectorState.WaitingPhone(
                        IpInfo(this.inn, this.ogrn, this.fullNameOfHolder, this.dataOgrn, it.content.text)
                    )
                }
            }
        }
        state<IpCollectorState.WaitingPhone> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.Phone) }
            onText {
                if (IsPhoneNumberValid(it.content.text)) {
                    state.override {
                        IpCollectorState.WaitingEmail(mainInfo, it.content.text)
                    }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.Phone)
                    return@onText
                }
            }
        }
        state<IpCollectorState.WaitingEmail> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.Email) }
            onText {
                val email = Email.of(it.content.text)
                if (email !=null) {
                    val info = EntrepreneurInformation(
                        state.snapshot.mainInfo, state.snapshot.phone, it.content.text
                    )
                    this@collector.exit(state, listOf(info))
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.Email)
                    return@onText
                }
            }
        }
    }
}
