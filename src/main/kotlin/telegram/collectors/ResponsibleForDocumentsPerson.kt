package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.ResponsibleForDocumentsPerson
import domain.entities.Email
import domain.entities.Fio
import domain.entities.PhoneNumber
import telegram.entities.state.ResponsibleForDocumentsPersonState
import telegram.resources.strings.CollectorStrings
import telegram.resources.strings.InvalidInputStrings

fun CollectorMapBuilder.responsibleForDocumentsPersonCollector() {
    collector<ResponsibleForDocumentsPerson>(initialState = ResponsibleForDocumentsPersonState.WaitingForFio) {
        state<ResponsibleForDocumentsPersonState.WaitingForFio> {
            onEnter { sendTextMessage(it, CollectorStrings.ResponsibleForDocumentsPerson.fio) }
            onText {
                val fio = Fio.of(it.content.text)
                if (fio != null) {
                    state.override { ResponsibleForDocumentsPersonState.WaitingForContactPhoneNumber(fio) }
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.Invalidfio)
                }
            }
        }
        state<ResponsibleForDocumentsPersonState.WaitingForContactPhoneNumber> {
            onEnter { sendTextMessage(it, CollectorStrings.ResponsibleForDocumentsPerson.ContactPhoneNumber) }
            onText {
                val contactPhoneNumber = PhoneNumber.of(it.content.text)
                if (contactPhoneNumber != null) {
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
                val email = Email.of(it.content.text)
                if (email != null) {
                    val responsibleForDocumentsPerson = ResponsibleForDocumentsPerson(
                        state.snapshot.fio,
                        state.snapshot.contactPhoneNumber,
                        email
                    )
                    this@collector.exit(state, responsibleForDocumentsPerson)
                } else {
                    sendTextMessage(it.chat.id, InvalidInputStrings.InvalidEmail)
                }
            }
        }
    }
}
