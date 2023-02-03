package telegram.flows

import com.ithersta.tgbotapi.menu.builders.menu
import dev.inmo.tgbotapi.extensions.api.send.media.sendDocument
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.requests.abstracts.FileId
import dev.inmo.tgbotapi.requests.abstracts.MultipartFile
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import io.github.classgraph.ClassGraph
import io.ktor.utils.io.streams.*
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.entities.state.FillingProvisionOfServicesState
import telegram.resources.strings.ButtonStrings
import telegram.resources.strings.Strings
import java.util.concurrent.ConcurrentHashMap

val mainMenu = menu<DialogState, Unit, _>(Strings.Menu.Message, EmptyState) {
    button(ButtonStrings.ChoiceFillingDoc.ProvisionOfServices) {
        state.override { FillingProvisionOfServicesState.BeginFillDoc }
    }
    button(ButtonStrings.ChoiceFillingDoc.DeliveryOfGoods) { message ->
        sendTextMessage(message.chat, Strings.InProcess, replyMarkup = ReplyKeyboardRemove())
        state.override { EmptyState }
    }
    button(ButtonStrings.ChoiceFillingDoc.GphContract) { message ->
        sendTextMessage(message.chat, Strings.InProcess, replyMarkup = ReplyKeyboardRemove())
        state.override { EmptyState }
    }
    button(ButtonStrings.ChoiceFillingDoc.SendDocs, FillingProvisionOfServicesState.WaitingForDocs(replyTo = null))

    val fileCache = ConcurrentHashMap<String, FileId>()
    button(ButtonStrings.ChoiceFillingDoc.ViewingExample) { message ->
        sendTextMessage(message.chat, Strings.commercialOfferPrompt(), replyMarkup = ReplyKeyboardRemove())
        ClassGraph().acceptPaths("/prompt").scan().use { scanResult ->
            scanResult.allResources.map { resource ->
                fileCache[resource.path]?.let { fileId ->
                    sendDocument(message.chat, fileId)
                } ?: run {
                    fileCache[resource.path] = sendDocument(
                        message.chat,
                        MultipartFile(resource.path.substringAfterLast("/")) { resource.open().asInput() }
                    ).content.media.fileId
                }
            }
        }
        state.override { EmptyState }
    }
}
