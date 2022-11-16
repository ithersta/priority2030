package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.FinanciallyResponsiblePerson
import org.apache.commons.validator.routines.EmailValidator
import telegram.entities.state.FinanciallyResponsiblePersonState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings
import validation.IsFullNameValid
import validation.IsPhoneNumberValid

fun CollectorMapBuilder.financiallyResponsiblePersonCollector() {
    collector<FinanciallyResponsiblePerson>(initialState = FinanciallyResponsiblePersonState.WaitingForFIO) {
        state<FinanciallyResponsiblePersonState.WaitingForFIO> {
            onEnter { sendTextMessage(it, CollectorStrings.FinanciallyResponsiblePerson.FIO) }
            onText {
                val FIO = it.content.text
                if (IsFullNameValid(FIO)) {
                    state.override { FinanciallyResponsiblePersonState.WaitingForContactPhoneNumber(FIO) }
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.InvalidFIO)
                }
            }
        }
        state<FinanciallyResponsiblePersonState.WaitingForContactPhoneNumber> {
            onEnter { sendTextMessage(it, CollectorStrings.FinanciallyResponsiblePerson.ContactPhoneNumber) }
            onText {
                val contactPhoneNumber = it.content.text
                if (IsPhoneNumberValid(contactPhoneNumber)) {
                    state.override {
                        FinanciallyResponsiblePersonState.WaitingForWorkPhoneNumber(
                            state.snapshot.FIO,
                            contactPhoneNumber
                        )
                    }
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.InvalidPhoneNumber)
                }
            }
        }

        state<FinanciallyResponsiblePersonState.WaitingForWorkPhoneNumber> {
            onEnter { sendTextMessage(it, CollectorStrings.FinanciallyResponsiblePerson.WorkPhoneNumber) }
            onText {
                val workPhoneNumber = it.content.text
                if (IsPhoneNumberValid(workPhoneNumber)) {
                    val financiallyResponsiblePerson=FinanciallyResponsiblePerson(
                        state.snapshot.FIO,
                        state.snapshot.contactPhoneNumber,
                        workPhoneNumber
                    )
                    this@collector.exit(state, listOf(financiallyResponsiblePerson))
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.InvalidPhoneNumber)
                }
            }
        }
    }
}