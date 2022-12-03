package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.utils.row
import domain.datatypes.CompanyInformation
import domain.datatypes.OrgInfo
import domain.entities.Email
import parser.Parser
import telegram.entities.state.CompanyCollectorState
import telegram.resources.strings.CollectorStrings
import validation.*

fun CollectorMapBuilder.organizationInfoCollector() {
    collector<CompanyInformation>(initialState = CompanyCollectorState.WaitingForInn) {
        val parser = Parser()
        state<CompanyCollectorState.WaitingForInn> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.Inn) }
            onText {
                if (IsInnValidForOoo(it.content.text)) {
                    state.override { CompanyCollectorState.WaitingForKpp(it.content.text) }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.InnForOoo)
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.WaitingForKpp> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.Kpp) }
            onText {
                if (IsKppValid(it.content.text)) {
                    val mainInfo = parser.parsing(state.snapshot.inn, it.content.text)
                    if (mainInfo != null) {
                        if (mainInfo.inn == "0") {
                            sendTextMessage(it.chat, CollectorStrings.Recommendations.IsWrongOrg)
                            state.override { CompanyCollectorState.WaitingForInn }
                        } else {
                            state.override {
                                CompanyCollectorState.WaitingInspection(mainInfo, mainInfo.abbreviatedNameOfOrg)
                            }
                        }
                    } else {
                        state.override { CompanyCollectorState.HandsWaitingOgrn(this.inn, it.content.text) }
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
                if (IsOgrnipValidForOoo(it.content.text)) {
                    state.override {
                        CompanyCollectorState.HandsWaitingFullNameOfOrg(this.inn, this.kpp, it.content.text)
                    }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.OgrnForOoo)
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingFullNameOfOrg> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.FullNameOfOrg) }
            onText {
                state.override {
                    CompanyCollectorState.HandsWaitingFullNameOfHolder(this.inn, this.kpp, this.ogrn, it.content.text)
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingFullNameOfHolder> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.Employee) }
            onText {
                if (IsFullNameValid(it.content.text)) {
                    state.override {
                        CompanyCollectorState.HandsWaitingPost(
                            this.inn, this.kpp, this.ogrn, this.fullNameOfOrg, it.content.text
                        )
                    }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.FullName)
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingPost> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.EmployeeRank) }
            onText {
                state.override {
                    CompanyCollectorState.HandsWaitingLocation(
                        this.inn, this.kpp, this.ogrn, this.fullNameOfOrg, this.fullNameOfHolder, it.content.text
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
                            this.inn, this.kpp, this.ogrn, this.fullNameOfOrg, this.post, this.fullNameOfHolder,
                            it.content.text
                        )
                    )
                }
            }
        }
        state<CompanyCollectorState.WaitingInspection> {
            onEnter {
                sendTextMessage(it, CollectorStrings.Ooo.isRight(state.snapshot.fullNameOfOrg.replace("ИП", "")),
                    replyMarkup = replyKeyboard(
                        resizeKeyboard = true,
                        oneTimeKeyboard = true
                    ) {
                        row {
                            simpleButton(CollectorStrings.Ooo.Yes)
                        }
                        row {
                            simpleButton(CollectorStrings.Ooo.No)
                        }
                    }
                )
            }
            onText { message ->
                when (message.content.text) {
                    CollectorStrings.Ooo.Yes -> {
                        state.override {
                            CompanyCollectorState.WaitingPhone(
                                mainInfo
                            )
                        }
                    }

                    CollectorStrings.Ooo.No -> {
                        state.override { CompanyCollectorState.WaitingForInn }
                    }

                    else -> {
                        sendTextMessage(message.chat, CollectorStrings.Ooo.invalid)
                        return@onText
                    }
                }
            }
        }
        state<CompanyCollectorState.WaitingPhone> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.Phone) }
            onText {
                if (IsPhoneNumberValid(it.content.text)) {
                    state.override { CompanyCollectorState.WaitingEmail(mainInfo, it.content.text) }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.Phone)
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.WaitingEmail> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.Email) }
            onText {
                val email = Email.of(it.content.text)
                if (email != null) {
                    val info = CompanyInformation(state.snapshot.mainInfo, state.snapshot.phone, it.content.text)
                    this@collector.exit(state, listOf(info))
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.Email)
                    return@onText
                }
            }
        }
    }
}
