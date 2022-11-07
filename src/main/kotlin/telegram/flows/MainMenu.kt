package telegram.flows

import com.ithersta.tgbotapi.menu.builders.menu
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.entities.state.FillingProvisionOfServicesState
import telegram.resources.strings.ButtonStrings
import telegram.resources.strings.Strings

val mainMenu = menu<DialogState, Unit, _>(Strings.Menu.Message, EmptyState) {
//    button(Strings.Menu.CreateDocuments) { message ->
//        sendTextMessage(message.chat, Strings.CreateDocumentsMessage, replyMarkup = ReplyKeyboardRemove())
//        state.override { CollectingDataState(emptyList()) }
//    }
    button(ButtonStrings.ChoiceFillingDoc.ProvisionOfServices) { message ->
        sendTextMessage(message.chat, Strings.CreateDocumentsMessage, replyMarkup = ReplyKeyboardRemove())
        state.override { FillingProvisionOfServicesState.BeginningFillDoc}
    }
    button(ButtonStrings.ChoiceFillingDoc.DeliveryOfGoods) { message ->
        //sendTextMessage(message.chat, Strings.CreateDocumentsMessage, replyMarkup = ReplyKeyboardRemove())
    }
    button(ButtonStrings.ChoiceFillingDoc.ConclusionGphCOntract) { message ->
        //sendTextMessage(message.chat, Strings.CreateDocumentsMessage, replyMarkup = ReplyKeyboardRemove())
    }
    button(ButtonStrings.ChoiceFillingDoc.ViewingExample) { message ->
        //sendTextMessage(message.chat, Strings.CreateDocumentsMessage, replyMarkup = ReplyKeyboardRemove())
    }
}
