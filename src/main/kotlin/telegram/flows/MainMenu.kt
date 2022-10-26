package telegram.flows

import com.ithersta.tgbotapi.menu.builders.menu
import telegram.entities.state.CollectingDataState
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.resources.strings.Strings

val mainMenu = menu<DialogState, Unit, _>(Strings.Menu.Message, EmptyState) {
    button(Strings.Menu.CreateDocuments, CollectingDataState(emptyList()))
}
