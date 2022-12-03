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
import domain.entities.IpInn
import services.Parser
import telegram.entities.state.IpCollectorState
import telegram.resources.strings.CollectorStrings
import validation.IsFullNameValid
import validation.IsOgrnipValidForIp
import validation.IsPhoneNumberValid


fun CollectorMapBuilder.ipInfoCollector() {
    collector<EntrepreneurInformation>(initialState = IpCollectorState.WaitingForInn) {
        val parser = Parser()
        state<IpCollectorState.WaitingForInn> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.inn) }
            onText { message ->
                val inn = IpInn.of(message.content.text)
                if (inn != null) {
                    val mainInfo = parser.getIpInfo(inn)
                    if (mainInfo != null) {
                        state.override { IpCollectorState.WaitingInspection(mainInfo, mainInfo.fullNameOfHolder) }
                    } else {
                        state.override { IpCollectorState.HandsWaitingOgrn(inn) }
                    }
                } else {
                    sendTextMessage(message.chat, CollectorStrings.Recommendations.innForIp)
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
                            simpleButton(CollectorStrings.IP.yes)
                        }
                        row {
                            simpleButton(CollectorStrings.IP.no)
                        }
                    })
            }
            onText { message ->
                when (message.content.text) {
                    CollectorStrings.IP.yes -> {
                        state.override { IpCollectorState.WaitingPhone(mainInfo) }
                    }

                    CollectorStrings.IP.no -> {
                        state.override { IpCollectorState.WaitingForInn }
                    }

                    else -> {
                        sendTextMessage(message.chat, CollectorStrings.IP.invalid)
                        return@onText
                    }
                }
            }
        }
        state<IpCollectorState.HandsWaitingOgrn> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.ogrn) }
            onText {
                if (IsOgrnipValidForIp(it.content.text)) {
                    state.override { IpCollectorState.HandsWaitingDataOfOgrn(this.inn, it.content.text) }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.ogrnForIp)
                    return@onText
                }
            }
        }
        state<IpCollectorState.HandsWaitingDataOfOgrn> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.data) }
            onText {
                state.override { IpCollectorState.HandsWaitingfullNameOfHolder(this.inn, this.ogrn, it.content.text) }
            }
        }
        state<IpCollectorState.HandsWaitingfullNameOfHolder> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.fullName) }
            onText {
                if (IsFullNameValid(it.content.text)) {
                    state.override {
                        IpCollectorState.HandsWaitingLocation(this.inn, this.ogrn, this.dataOgrn, it.content.text)
                    }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.fullName)
                    return@onText
                }
            }
        }
        state<IpCollectorState.HandsWaitingLocation> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.location) }
            onText {
                state.override {
                    IpCollectorState.WaitingPhone(
                        IpInfo(inn, ogrn, fullNameOfHolder, dataOgrn, it.content.text)
                    )
                }
            }
        }
        state<IpCollectorState.WaitingPhone> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.phone) }
            onText {
                if (IsPhoneNumberValid(it.content.text)) {
                    state.override {
                        IpCollectorState.WaitingEmail(mainInfo, it.content.text)
                    }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.phone)
                    return@onText
                }
            }
        }
        state<IpCollectorState.WaitingEmail> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.email) }
            onText {
                val email = Email.of(it.content.text)
                if (email != null) {
                    val info = EntrepreneurInformation(
                        state.snapshot.mainInfo, state.snapshot.phone, it.content.text
                    )
                    this@collector.exit(state, listOf(info))
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.email)
                    return@onText
                }
            }
        }
    }
}
