package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.utils.row
import domain.datatypes.EntrepreneurInformation
import domain.entities.*
import services.SbisParser
import telegram.entities.state.IpCollectorState
import telegram.resources.strings.ButtonStrings
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings
import telegram.resources.strings.InvalidInputStrings.InvalidAnswer
import telegram.resources.strings.InvalidInputStrings.InvalidEmail
import telegram.resources.strings.InvalidInputStrings.InvalidPhoneNumber
import validation.IsFullNameValid

@Suppress("LongMethod")
fun CollectorMapBuilder.ipInfoCollector() {
    collector<EntrepreneurInformation>(initialState = IpCollectorState.WaitingForInn) {
        val parser = SbisParser()
        state<IpCollectorState.WaitingForInn> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.Inn) }
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
                            simpleButton(ButtonStrings.No)
                            simpleButton(ButtonStrings.Yes)
                        }
                    })
            }
            onText(ButtonStrings.Yes) { state.override { IpCollectorState.WaitingPhone(mainInfo) } }
            onText(ButtonStrings.No) { state.override { IpCollectorState.WaitingForInn } }
            onText { sendTextMessage(it.chat, InvalidAnswer) }
        }
        state<IpCollectorState.HandsWaitingOgrn> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.Ogrn) }
            onText {
                val ipOgrn = IpOgrn.of(it.content.text)
                if (ipOgrn != null) {
                    state.override { IpCollectorState.HandsWaitingDataOfOgrn(inn, ipOgrn) }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.OgrnForIp)
                    return@onText
                }
            }
        }
        state<IpCollectorState.HandsWaitingDataOfOgrn> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.Date) }
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
                        IpInfo(inn, ogrn, fullNameOfHolder, dataOgrn, it.content.text)
                    )
                }
            }
        }
        state<IpCollectorState.WaitingPhone> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.Phone) }
            onText {
                val phoneNumber = PhoneNumber.of(it.content.text)
                if (phoneNumber != null) {
                    state.override {
                        IpCollectorState.WaitingEmail(mainInfo, phoneNumber)
                    }
                } else {
                    sendTextMessage(it.chat, InvalidPhoneNumber)
                    return@onText
                }
            }
        }
        state<IpCollectorState.WaitingEmail> {
            onEnter { sendTextMessage(it, CollectorStrings.IP.Email) }
            onText {
                val email = Email.of(it.content.text)
                if (email != null) {
                    val info = EntrepreneurInformation(state.snapshot.mainInfo, state.snapshot.phone, email)
                    this@collector.exit(state, listOf(info))
                } else {
                    sendTextMessage(it.chat, InvalidEmail)
                    return@onText
                }
            }
        }
    }
}
