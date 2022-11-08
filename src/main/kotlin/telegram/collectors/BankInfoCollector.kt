package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.BankInfo
import telegram.entities.state.BankCollectorState
import telegram.resources.strings.CollectorStrings

fun CollectorMapBuilder.BankInfoCollector() {
    collector<BankInfo>(initialState = BankCollectorState.WaitingForBik) {
        state<BankCollectorState.WaitingForBik> {
            onEnter { sendTextMessage(it, CollectorStrings.Bank.bik) }
            onText {
                // возьмем данные с сайта ...
                state.override { BankCollectorState.WaitingForPaymentAccount(it.content.text, "1", "bank") }
            }
        }
        state<BankCollectorState.WaitingForPaymentAccount> {
            onEnter { sendTextMessage(it, CollectorStrings.Bank.account) }
            onText { message ->
                val info = BankInfo(
                    bik = state.snapshot.bik,
                    correspondentAccount = state.snapshot.corrAccount,
                    bankName = state.snapshot.nameBank,
                    settlementAccountNumber = message.content.text
                )
                this@collector.exit(state, listOf(info))
            }
        }
    }
}