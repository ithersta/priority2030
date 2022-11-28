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
    button(ButtonStrings.ChoiceFillingDoc.ProvisionOfServices) { message ->
        state.override { FillingProvisionOfServicesState.BeginFillDoc}
    }
    button(ButtonStrings.ChoiceFillingDoc.DeliveryOfGoods) { message ->
        sendTextMessage(message.chat, Strings.InProcess, replyMarkup = ReplyKeyboardRemove())
        state.override { EmptyState }
    }
    button(ButtonStrings.ChoiceFillingDoc.ConclusionGphCOntract) { message ->
        sendTextMessage(message.chat, Strings.InProcess, replyMarkup = ReplyKeyboardRemove())
        state.override { EmptyState }
    }
    button(ButtonStrings.ChoiceFillingDoc.ViewingExample) { message ->
        sendTextMessage(message.chat, Strings.comemrcialOfferHint(), replyMarkup = ReplyKeyboardRemove())
        state.override { EmptyState }
    }
}
