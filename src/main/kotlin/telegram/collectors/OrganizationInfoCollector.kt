package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.CompanyInfo
import parser.Parser
import telegram.entities.state.CompanyCollectorState
import telegram.resources.strings.CollectorStrings
import validation.*

fun CollectorMapBuilder.organizationInfoCollector() {
    collector<CompanyInfo>(initialState = CompanyCollectorState.WaitingForInn) {
        val parser = Parser()
        state<CompanyCollectorState.WaitingForInn> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.inn) }
            onText {
                if (IsInnValidForIp(it.content.text)) {
                    state.override {
                        CompanyCollectorState.WaitingForKpp(
                            inn = it.content.text
                        )
                    }
                } else {
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.WaitingForKpp> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.kpp) }
            onText {
                if (IsInnValidForOoo(it.content.text)) {
                    if (parser.parsing(state.snapshot.inn, it.content.text) == 200) {
                        state.override {
                            CompanyCollectorState.WaitingInspection(
                                inn = it.content.text,
                                fullNameOfOrg = parser.fullNameOfOrg
                            )
                        }
                    } else {
                        state.override {
                            CompanyCollectorState.HandsWaitingOgrn(
                                inn = state.snapshot.inn,
                                kpp = it.content.text
                            )
                        }
                    }
                } else {
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingOgrn> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.ogrn) }
            onText {
                if (IsOgrnipValidForOoo(it.content.text)) {
                    state.override {
                        CompanyCollectorState.HandsWaitingFullNameOfOrg(
                            inn = state.snapshot.inn,
                            kpp = state.snapshot.kpp,
                            ogrn = it.content.text
                        )
                    }
                } else {
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingFullNameOfOrg> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.fullNameofOrg) }
            onText {
//              TODO: какие-то образом надо проверить!
                state.override {
                    CompanyCollectorState.HandsWaitingFullNameOfHolder(
                        inn = state.snapshot.inn,
                        kpp = state.snapshot.kpp,
                        ogrn = state.snapshot.ogrn,
                        fullNameOfOrg = it.content.text
                    )
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingFullNameOfHolder> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.employee) }
            onText {
//              TODO: проверка для полного ФИО а у нас только проверка для краткой формы ФИО.
                state.override {
                    CompanyCollectorState.HandsWaitingPost(
                        inn = state.snapshot.inn,
                        kpp = state.snapshot.kpp,
                        ogrn = state.snapshot.ogrn,
                        fullNameOfOrg = state.snapshot.fullNameOfOrg,
                        fullNameOfHolder = it.content.text
                    )
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingPost> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.employeeRank) }
            onText {
//              Не возможно проверить !
                state.override {
                    CompanyCollectorState.HandsWaitingLocation(
                        inn = state.snapshot.inn,
                        kpp = state.snapshot.kpp,
                        ogrn = state.snapshot.ogrn,
                        fullNameOfOrg = state.snapshot.fullNameOfOrg,
                        fullNameOfHolder = state.snapshot.fullNameOfHolder,
                        post = it.content.text
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
                        inn = state.snapshot.inn,
                        kpp = state.snapshot.kpp,
                        ogrn = state.snapshot.ogrn,
                        fullNameOfOrg = state.snapshot.fullNameOfOrg,
                        fullNameOfHolder = state.snapshot.fullNameOfHolder,
                        post = state.snapshot.post,
                        location = it.content.text
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
                            inn = parser.innOfOrg,
                            kpp = parser.kppOfOrg,
                            ogrn = parser.ogrnOfOrg,
                            fullNameOfOrg = parser.fullNameOfOrg,
                            fullNameOfHolder = parser.fullNameOfHolder,
                            post = parser.post,
                            location = parser.location
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
                    state.override {
                        CompanyCollectorState.WaitingEmail(
                            inn = state.snapshot.inn,
                            kpp = state.snapshot.kpp,
                            ogrn = state.snapshot.ogrn,
                            fullNameOfOrg = state.snapshot.fullNameOfOrg,
                            fullNameOfHolder = state.snapshot.fullNameOfHolder,
                            post = state.snapshot.post,
                            location = state.snapshot.location,
                            phone = it.content.text
                        )
                    }
                } else {
                    return@onText
                }
            }
        }
        state<CompanyCollectorState.WaitingEmail> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.email) }
            onText {
                if (IsEmailValid(it.content.text)) {
                    val info = CompanyInfo(
                        inn = state.snapshot.inn,
                        kpp = state.snapshot.kpp,
                        ogrn = state.snapshot.ogrn,
                        fullNameOfOrg = state.snapshot.fullNameOfOrg,
                        fullNameOfHolder = state.snapshot.fullNameOfHolder,
                        post = state.snapshot.post,
                        location = state.snapshot.location,
                        phone = state.snapshot.phone,
                        email = it.content.text
                    )
                    this@collector.exit(state, listOf(info))
                } else {
                    return@onText
                }
            }
        }
    }
}
