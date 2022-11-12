package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.CompanyInfo
import parser.Parser
import telegram.entities.state.CompanyCollectorState
import telegram.resources.strings.CollectorStrings

fun CollectorMapBuilder.organizationInfoCollector() {
    collector<CompanyInfo>(initialState = CompanyCollectorState.WaitingForInn) {
        val parser = Parser()
        state<CompanyCollectorState.WaitingForInn> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.inn) }
            onText {
                state.override { CompanyCollectorState.WaitingForKpp(it.content.text) }
            }
        }
        state<CompanyCollectorState.WaitingForKpp> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.kpp) }
            onText {
                if (parser.parsing(state.snapshot.inn, it.content.text) == 200) {
                    state.override { CompanyCollectorState.WaitingInspection(it.content.text, parser.fullNameOfOrg) }
                } else {
                    state.override { CompanyCollectorState.HandsWaitingOgrn(state.snapshot.inn, it.content.text) }
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingOgrn> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.ogrn) }
            onText {
                state.override {
                    CompanyCollectorState.HandsWaitingOkpo(state.snapshot.inn, state.snapshot.kpp, it.content.text)
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingOkpo> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.okpo) }
            onText {
                state.override {
                    CompanyCollectorState.HandsWaitingFullNameOfOrg(
                        state.snapshot.inn, state.snapshot.kpp, state.snapshot.ogrn, it.content.text
                    )
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingFullNameOfOrg> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.fullNameofOrg) }
            onText {
                state.override {
                    CompanyCollectorState.HandsWaitingFullNameOfHolder(
                        state.snapshot.inn, state.snapshot.kpp, state.snapshot.ogrn, state.snapshot.okpo,
                        it.content.text
                    )
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingFullNameOfHolder> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.employee) }
            onText {
                state.override {
                    CompanyCollectorState.HandsWaitingPost(
                        state.snapshot.inn, state.snapshot.kpp, state.snapshot.ogrn, state.snapshot.okpo,
                        state.snapshot.fullNameOfOrg, it.content.text
                    )
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingPost> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.employeeRank) }
            onText {
                state.override {
                    CompanyCollectorState.HandsWaitingLocation(
                        state.snapshot.inn, state.snapshot.kpp, state.snapshot.ogrn, state.snapshot.okpo,
                        state.snapshot.fullNameOfOrg, state.snapshot.fullNameOfHolder, it.content.text
                    )
                }
            }
        }
        state<CompanyCollectorState.HandsWaitingLocation> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.location) }
            onText {
                state.override {
                    CompanyCollectorState.WaitingPhone(
                        state.snapshot.inn, state.snapshot.kpp, state.snapshot.ogrn, state.snapshot.okpo,
                        state.snapshot.fullNameOfOrg, state.snapshot.fullNameOfHolder, state.snapshot.post,
                        it.content.text
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
                if (response.equals("Да")) {
                    state.override {
                        CompanyCollectorState.WaitingPhone(
                            parser.innOfOrg, parser.kppOfOrg, parser.ogrnOfOrg, parser.okpoOfOrg,
                            parser.fullNameOfOrg, parser.fullNameOfHolder, parser.post, parser.location
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
                state.override {
                    CompanyCollectorState.WaitingEmail(
                        state.snapshot.inn, state.snapshot.kpp, state.snapshot.ogrn,
                        state.snapshot.okpo,
                        state.snapshot.fullNameOfOrg,
                        state.snapshot.fullNameOfHolder,
                        state.snapshot.post,
                        state.snapshot.location,
                        it.content.text
                    )
                }
            }
        }
        state<CompanyCollectorState.WaitingEmail> {
            onEnter { sendTextMessage(it, CollectorStrings.Ooo.email) }
            onText {
                val info = CompanyInfo(
                    inn = state.snapshot.inn,
                    kpp = state.snapshot.kpp,
                    ogrn = state.snapshot.ogrn,
                    okpo = state.snapshot.okpo,
                    fullNameOfOrg = state.snapshot.fullNameOfOrg,
                    fullNameOfHolder = state.snapshot.fullNameOfHolder,
                    post = state.snapshot.post,
                    location = state.snapshot.location,
                    phone = state.snapshot.phone,
                    email = it.content.text
                )
                this@collector.exit(state, listOf(info))
            }
        }
    }
}
