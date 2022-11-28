package telegram.flows

import com.ithersta.tgbotapi.menu.builders.menu
import dev.inmo.tgbotapi.extensions.api.send.media.sendDocument
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import io.ktor.utils.io.streams.*
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
        sendTextMessage(message.chat, Strings.comemrcialOfferPrompt(), replyMarkup = ReplyKeyboardRemove())
        val prompt1 = object {}::class.java.getResourceAsStream("/prompt/ArticMedia_Коммерческое_предложение_ФГАОУ_ВО_СПбПУ_Внедрение_Битрикс24.pdf")?: run {
            return@button
        }
        val prompt2 = object {}::class.java.getResourceAsStream("/prompt/Asanov КП.pdf")?: run {
            return@button
        }
        val prompt3 = object {}::class.java.getResourceAsStream("/prompt/КП_Политех_Nimax.pdf")?: run {
            return@button
        }
        sendDocument(message.chat, MultipartFile("ArticMedia_Коммерческое_предложение_ФГАОУ_ВО_СПбПУ_Внедрение_Битрикс24.pdf") { prompt1.asInput() })
        sendDocument(message.chat, MultipartFile("Asanov КП.pdf") { prompt2.asInput() })
        sendDocument(message.chat, MultipartFile("КП_Политех_Nimax.pdf") { prompt3.asInput() })
        state.override { EmptyState }
    }
}
