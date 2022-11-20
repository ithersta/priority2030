package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.FinanciallyResponsiblePerson
import telegram.entities.state.FinanciallyResponsiblePersonState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings
import validation.IsFullNameValid
import validation.IsPhoneNumberValid

fun CollectorMapBuilder.financiallyResponsiblePersonCollector() {
    collector<FinanciallyResponsiblePerson>(initialState = FinanciallyResponsiblePersonState.WaitingForfio) {
        state<FinanciallyResponsiblePersonState.WaitingForfio> {
            onEnter { sendTextMessage(it, CollectorStrings.FinanciallyResponsiblePerson.fio) }
            onText {
                val fio = it.content.text
                if (IsFullNameValid(fio)) {
                    state.override { FinanciallyResponsiblePersonState.WaitingForContactPhoneNumber(fio) }
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.Invalidfio)
                }
            }
        }
        state<FinanciallyResponsiblePersonState.WaitingForContactPhoneNumber> {
            onEnter { sendTextMessage(it, CollectorStrings.FinanciallyResponsiblePerson.ContactPhoneNumber) }
            onText {
                val contactPhoneNumber = it.content.text
                if (IsPhoneNumberValid(contactPhoneNumber)) {
                        val financiallyResponsiblePerson=FinanciallyResponsiblePerson(
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
