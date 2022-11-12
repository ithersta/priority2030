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
                            parser.innOfOrg,
                            parser.kppOfOrg,
                            parser.orgnOfOrg,
                            parser.okpoOfOrg,
                            parser.fullNameOfOrg,
                            parser.fullNameOfHolder,
                            parser.post,
                            parser.location
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
                        state.snapshot.inn,
                        state.snapshot.kpp,
                        state.snapshot.orgn,
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
                    orgn = state.snapshot.orgn,
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
