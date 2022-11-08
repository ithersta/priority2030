package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.BankInfo
import parser.ParserBik
import telegram.entities.state.BankCollectorState
import telegram.resources.strings.CollectorStrings

fun CollectorMapBuilder.BankInfoCollector() {
    collector<BankInfo>(initialState = BankCollectorState.WaitingForBik) {
        state<BankCollectorState.WaitingForBik> {
            onEnter { sendTextMessage(it, CollectorStrings.Bank.bik) }
            onText {
                state.override {
                    BankCollectorState.WaitingForPaymentAccount(it.content.text)
                }
            }
        }
        state<BankCollectorState.WaitingForPaymentAccount> {
            onEnter { sendTextMessage(it, CollectorStrings.Bank.account) }
            onText { message ->
                val parser = ParserBik()
                parser.parseWebPage(state.snapshot.bik)
                val info = BankInfo(
                    bik = state.snapshot.bik,
                    correspondentAccount =  parser.corrAccount,
                    bankName = parser.bakName,
                    settlementAccountNumber = message.content.text
                )
                this@collector.exit(state, listOf(info))
            }
        }
    }
}