package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.CompanyInformation
import domain.datatypes.OrgInfo
import parser.ConstantsForParsing.statusCodeSuccessful
import parser.Parser
import telegram.entities.state.CompanyCollectorState
import telegram.resources.strings.CollectorStrings
import validation.*

fun CollectorMapBuilder.organizationInfoCollector() {
    collector<CompanyInformation>(initialState = CompanyCollectorState.WaitingForInn) {
        val parser = Parser()
        state<CompanyCollectorState.WaitingForInn> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.inn) }
            onText {
                if (IsInnValidForOoo(it.content.text)) {
                    state.override { CompanyCollectorState.WaitingForKpp(it.content.text) }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.innForOoo)
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.WaitingForKpp> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.kpp) }
            onText {
                if (IsKppValid(it.content.text)) {
                    val mainInfo = parser.parsing(state.snapshot.inn, it.content.text)
                    if (mainInfo != null) {
                        state.override { CompanyCollectorState.WaitingInspection(mainInfo, mainInfo.fullNameOfOrg) }
                    } else {
                        state.override { CompanyCollectorState.HandsWaitingOgrn(this.inn, it.content.text) }
                    }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.kpp)
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingOgrn> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.ogrn) }
            onText {
                if (IsOgrnipValidForOoo(it.content.text)) {
                    state.override {
                        CompanyCollectorState.HandsWaitingFullNameOfOrg(this.inn, this.kpp, it.content.text)
                    }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.ogrnForOoo)
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingFullNameOfOrg> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.fullNameofOrg) }
            onText {
                state.override {
                    CompanyCollectorState.HandsWaitingFullNameOfHolder(this.inn, this.kpp, this.ogrn, it.content.text)
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingFullNameOfHolder> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.employee) }
            onText {
                if (IsFullNameValid(it.content.text)) {
                    state.override {
                        CompanyCollectorState.HandsWaitingPost(
                            this.inn, this.kpp, this.ogrn, this.fullNameOfOrg, it.content.text
                        )
                    }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.fullName)
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingPost> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.employeeRank) }
            onText {
//              Не возможно проверить !
                state.override {
                    CompanyCollectorState.HandsWaitingLocation(
                        this.inn, this.kpp, this.ogrn, this.fullNameOfOrg, this.fullNameOfHolder, it.content.text
                    )
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingLocation> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.location) }
            onText {
//              Не возможно проверить !
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
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.isRight(state.snapshot.fullNameOfOrg)) }
            onText { message ->
                val response = when (message.content.text) {
                    CollectorStrings.Ooo.Yes -> "Да"
                    CollectorStrings.Ooo.No -> "Нет"
                    else -> {
                        sendTextMessage(message.chat, CollectorStrings.Ooo.Invalid)
                        return@onText
                    }
                }
                if (response == "Да") {
                    state.override {
                        CompanyCollectorState.WaitingPhone(
                            mainInfo
                        )
                    }
                } else {
                    state.override { CompanyCollectorState.WaitingForInn }
                }
            }
        }
        state<CompanyCollectorState.WaitingPhone> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.phone) }
            onText {
                if (IsPhoneNumberValid(it.content.text)) {
                    state.override { CompanyCollectorState.WaitingEmail(mainInfo, it.content.text) }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.phone)
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.WaitingEmail> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.email) }
            onText {
                if (IsEmailValid(it.content.text)) {
                    val info = CompanyInformation(state.snapshot.mainInfo, state.snapshot.phone, it.content.text)
                    this@collector.exit(state, listOf(info))
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.email)
                    return@onText
                }
            }
        }
    }
}
