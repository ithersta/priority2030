package telegram

import com.ithersta.tgbotapi.fsm.builders.StateFilterBuilder
import dev.inmo.tgbotapi.types.UserId
import telegram.collectors.*
import telegram.entities.state.CollectingDataState
import telegram.entities.state.DialogState

fun StateFilterBuilder<DialogState, Unit, CollectingDataState, Unit, UserId>.collectors() = buildCollectorMap {
    BankInfoCollector()
    fullNameCollector()
    organizationInfoCollector()
    IpInfoCollector()
    organizationTypeCollector()
}
