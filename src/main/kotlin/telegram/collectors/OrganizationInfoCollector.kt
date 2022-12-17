package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.utils.row
import domain.datatypes.CompanyInformation
import domain.entities.*
import services.SbisParser
import telegram.entities.state.CompanyCollectorState
import telegram.resources.strings.ButtonStrings
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings.InvalidAnswer
import telegram.resources.strings.InvalidInputStrings.InvalidEmail
import telegram.resources.strings.InvalidInputStrings.InvalidPhoneNumber

@Suppress("LongMethod")
fun CollectorMapBuilder.organizationInfoCollector() {
    collector<CompanyInformation>(initialState = CompanyCollectorState.WaitingForInn) {
        val parser = SbisParser()
        state<CompanyCollectorState.WaitingForInn> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.Inn) }
            onText {
                val inn = OooInn.of(it.content.text)
                if (inn != null) {
                    state.override { CompanyCollectorState.WaitingForKpp(inn) }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.InnForOoo)
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.WaitingForKpp> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.Kpp) }
            onText {
                val kpp = Kpp.of(it.content.text)
                if (kpp != null) {
                    val mainInfo = parser.getOrgInfo(state.snapshot.inn, kpp)
                    if (mainInfo != null) {
                        state.override {
                            CompanyCollectorState.WaitingInspection(mainInfo, mainInfo.fullName)
                        }
                    } else {
                        state.override { CompanyCollectorState.HandsWaitingOgrn(inn, kpp) }
                    }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.Kpp)
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingOgrn> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.Ogrn) }
            onText {
                val oooOgrn = OooOgrn.of(it.content.text)
                if (oooOgrn != null) {
                    state.override {
                        CompanyCollectorState.HandsWaitingFullNameOfOrg(inn, kpp, oooOgrn)
                    }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.OgrnForOoo)
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingFullNameOfOrg> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.FullNameOfOrganization) }
            onText {
                state.override {
                    CompanyCollectorState.HandsWaitingFullNameOfHolder(inn, kpp, ogrn, it.content.text)
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingFullNameOfHolder> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.Employee) }
            onText {
                state.override {
                    CompanyCollectorState.HandsWaitingPost(inn, kpp, ogrn, fullNameOfOrg, it.content.text)
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingPost> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.EmployeeRank) }
            onText {
                state.override {
                    CompanyCollectorState.HandsWaitingLocation(
                        inn,
                        kpp,
                        ogrn,
                        fullNameOfOrg,
                        fullNameOfHolder,
                        it.content.text
                    )
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingLocation> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.Location) }
            onText {
                state.override {
                    CompanyCollectorState.WaitingPhone(
                        OrgInfo(
                            inn,
                            kpp,
                            ogrn,
                            fullNameOfOrg,
                            post,
                            fullNameOfHolder,
                            it.content.text
                        )
                    )
                }
            }
        }
        state<CompanyCollectorState.WaitingInspection> {
            onEnter {
                sendTextMessage(
                    it,
                    CollectorStrings.Ooo.isRight(state.snapshot.fullNameOfOrg.replace("ИП", "")),
                    replyMarkup = replyKeyboard(
                        resizeKeyboard = true,
                        oneTimeKeyboard = true
                    ) {
                        row {
                            simpleButton(ButtonStrings.No)
                            simpleButton(ButtonStrings.Yes)
                        }
                    }
                )
            }
            onText(ButtonStrings.Yes) { state.override { CompanyCollectorState.WaitingPhone(mainInfo) } }
            onText(ButtonStrings.No) { state.override { CompanyCollectorState.WaitingForInn } }
            onText { sendTextMessage(it.chat, InvalidAnswer) }
        }
        state<CompanyCollectorState.WaitingPhone> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.Phone) }
            onText {
                val phoneNumber = PhoneNumber.of(it.content.text)
                if (phoneNumber != null) {
                    state.override { CompanyCollectorState.WaitingEmail(mainInfo, phoneNumber) }
                } else {
                    sendTextMessage(it.chat, InvalidPhoneNumber)
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.WaitingEmail> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.Email) }
            onText {
                val email = Email.of(it.content.text)
                if (email != null) {
                    val info = CompanyInformation(state.snapshot.mainInfo, state.snapshot.phone, email)
                    this@collector.exit(state, info)
                } else {
                    sendTextMessage(it.chat, InvalidEmail)
                    return@onText
                }
            }
        }
    }
}
