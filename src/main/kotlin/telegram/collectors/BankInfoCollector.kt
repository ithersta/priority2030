package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.Bank
import domain.datatypes.PaymentInformation
import domain.entities.Bic
import domain.entities.CorrespondentAccount
import domain.entities.SettlementAccount
import services.ParserBik
import telegram.entities.state.BankCollectorState
import telegram.resources.strings.CollectorStrings

fun CollectorMapBuilder.bankInfoCollector() {
    collector<PaymentInformation>(initialState = BankCollectorState.WaitingForBik) {
        state<BankCollectorState.WaitingForBik> {
            val parser = ParserBik()
            onEnter { sendTextMessage(it, CollectorStrings.Bank.bik) }
            onText {
                val bic = Bic.of(it.content.text)
                if (bic != null) {
                    val bank = parser.parseWebPage(bic)
                    if (bank != null) {
                        state.override {
                            BankCollectorState.WaitingForSettlementAccount(bank)
                        }
                    } else {
                        state.override { BankCollectorState.HandsWaitingForCorrAccount(bic) }
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
                val correspondentAccount = CorrespondentAccount.of(it.content.text)
                if (correspondentAccount != null) {
                    state.override { BankCollectorState.HandsWaitingForBankName(bic, correspondentAccount) }
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
                    BankCollectorState.WaitingForSettlementAccount(
                        Bank(this.bic, this.correspondentAccount, it.content.text)
                    )
                }
            }
        }
        state<BankCollectorState.WaitingForSettlementAccount> {
            onEnter { sendTextMessage(it, CollectorStrings.Bank.account) }
            onText { message ->
                val settlementAccount = SettlementAccount.of(message.content.text)
                if (settlementAccount != null) {
                    val info = PaymentInformation(
                        bank = state.snapshot.mainInfo,
                        settlementAccount = settlementAccount
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
