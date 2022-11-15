package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.BankInfo
import parser.ParserBik
import telegram.entities.state.BankCollectorState
import telegram.resources.strings.CollectorStrings
import validation.IsBicValid
import validation.IsCorrAccountValid
import validation.IsPaymentAccountValid

fun CollectorMapBuilder.BankInfoCollector() {
    collector<BankInfo>(initialState = BankCollectorState.WaitingForBik) {
        state<BankCollectorState.WaitingForBik> {
            onEnter { sendTextMessage(it, CollectorStrings.Bank.bik) }
            onText {
                val parser = ParserBik()
                if (IsBicValid(it.content.text)) {
                    if (parser.parseWebPage(it.content.text) == 200) {
                        state.override {
                            BankCollectorState.WaitingForPaymentAccount(
                                it.content.text, parser.corrAccount, parser.bakName
                            )
                        }
                    } else {
                        state.override { BankCollectorState.HandsWaitingForCorrAccount(it.content.text) }
                    }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.bik)
                    return@onText
                }
            }
        }
        state<BankCollectorState.HandsWaitingForCorrAccount> {
            onEnter { sendTextMessage(it, CollectorStrings.Bank.corrAccount) }
            onText {
                if (IsCorrAccountValid(it.content.text)) {
                    state.override { BankCollectorState.HandsWaitingForBankName(state.snapshot.bik, it.content.text) }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.corrAccount)
                    return@onText
                }
            }
        }
        state<BankCollectorState.HandsWaitingForBankName> {
            onEnter { sendTextMessage(it, CollectorStrings.Bank.bankName) }
            onText {
                state.override {
                    BankCollectorState.WaitingForPaymentAccount(
                        state.snapshot.bik, state.snapshot.correspondentAccount, it.content.text
                    )
                }
            }
        }
        state<BankCollectorState.WaitingForPaymentAccount> {
            onEnter { sendTextMessage(it, CollectorStrings.Bank.account) }
            onText { message ->
                if (IsPaymentAccountValid(message.content.text)) {
                    val info = BankInfo(
                        bik = state.snapshot.bik,
                        correspondentAccount = state.snapshot.correspondentAccount,
                        bankName = state.snapshot.bankName,
                        settlementAccountNumber = message.content.text
                    )
                    this@collector.exit(state, listOf(info))
                } else {
                    sendTextMessage(message.chat, CollectorStrings.Recommendations.paymentAccount)
                    return@onText
                }
            }
        }
    }
}