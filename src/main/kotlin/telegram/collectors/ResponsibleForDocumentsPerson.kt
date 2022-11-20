package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.ResponsibleForDocumentsPerson
import org.apache.commons.validator.routines.EmailValidator
import telegram.entities.state.ResponsibleForDocumentsPersonState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings
import validation.IsFullNameValid
import validation.IsPhoneNumberValid

fun CollectorMapBuilder.responsibleForDocumentsPersonCollector() {
    collector<ResponsibleForDocumentsPerson>(initialState = ResponsibleForDocumentsPersonState.WaitingForFIO) {
        state<ResponsibleForDocumentsPersonState.WaitingForFIO> {
            onEnter { sendTextMessage(it, CollectorStrings.ResponsibleForDocumentsPerson.FIO) }
            onText {
                val FIO = it.content.text
                if (IsFullNameValid(FIO)) {
                    state.override {ResponsibleForDocumentsPersonState.WaitingForContactPhoneNumber(FIO) }
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.InvalidFIO)
                }
            }
        }
        state<ResponsibleForDocumentsPersonState.WaitingForContactPhoneNumber> {
            onEnter { sendTextMessage(it,  CollectorStrings.ResponsibleForDocumentsPerson.ContactPhoneNumber) }
            onText {
                val contactPhoneNumber = it.content.text
                if (IsPhoneNumberValid(contactPhoneNumber)) {
                    state.override {
                        ResponsibleForDocumentsPersonState.WaitingForEmail(
                            this.FIO,
                            contactPhoneNumber
                        )
                    }
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.InvalidPhoneNumber)
                }
            }
        }

        state<ResponsibleForDocumentsPersonState.WaitingForEmail> {
            onEnter { sendTextMessage(it, CollectorStrings.ResponsibleForDocumentsPerson.Email) }
            onText {
                val email = it.content.text
                val emailValidator = EmailValidator.getInstance()
                if (emailValidator.isValid(email)) {
                    val responsibleForDocumentsPerson = ResponsibleForDocumentsPerson(
                        state.snapshot.FIO,
                        state.snapshot.contactPhoneNumber,
                        email
                    )
                    this@collector.exit(state, listOf(responsibleForDocumentsPerson))

                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.InvalidEmail)
                }
            }
        }
    }
}