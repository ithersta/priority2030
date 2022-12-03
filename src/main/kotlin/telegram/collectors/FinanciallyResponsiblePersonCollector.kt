package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.FinanciallyResponsiblePerson
import domain.entities.Fio
import domain.entities.PhoneNumber
import telegram.entities.state.FinanciallyResponsiblePersonState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings

fun CollectorMapBuilder.financiallyResponsiblePersonCollector() {
    collector<FinanciallyResponsiblePerson>(initialState = FinanciallyResponsiblePersonState.WaitingForFio) {
        state<FinanciallyResponsiblePersonState.WaitingForFio> {
            onEnter { sendTextMessage(it, CollectorStrings.FinanciallyResponsiblePerson.Fio) }
            onText {
                val fio = Fio.of(it.content.text)
                if (fio != null) {
                    state.override { FinanciallyResponsiblePersonState.WaitingForContactPhoneNumber(fio) }
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.Invalidfio)
                }
            }
        }
        state<FinanciallyResponsiblePersonState.WaitingForContactPhoneNumber> {
            onEnter { sendTextMessage(it, CollectorStrings.FinanciallyResponsiblePerson.ContactPhoneNumber) }
            onText {
                val contactPhoneNumber = PhoneNumber.of(it.content.text)
                if (contactPhoneNumber != null) {
                    val financiallyResponsiblePerson = FinanciallyResponsiblePerson(
                        state.snapshot.fio,
                        contactPhoneNumber
                    )
                    this@collector.exit(state, listOf(financiallyResponsiblePerson))
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.InvalidPhoneNumber)
                }
            }
        }
    }
}
