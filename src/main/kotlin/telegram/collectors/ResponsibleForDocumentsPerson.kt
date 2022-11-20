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
    collector<ResponsibleForDocumentsPerson>(initialState = ResponsibleForDocumentsPersonState.WaitingForfio) {
        state<ResponsibleForDocumentsPersonState.WaitingForfio> {
            onEnter { sendTextMessage(it, CollectorStrings.ResponsibleForDocumentsPerson.fio) }
            onText {
                val fio = it.content.text
                if (IsFullNameValid(fio)) {
                    state.override {ResponsibleForDocumentsPersonState.WaitingForContactPhoneNumber(fio) }
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.Invalidfio)
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
                            this.fio,
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
                        state.snapshot.fio,
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
