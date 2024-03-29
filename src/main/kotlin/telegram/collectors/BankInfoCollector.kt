package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.Bank
import domain.datatypes.PaymentDetails
import domain.entities.Bik
import domain.entities.CorrespondentAccount
import domain.entities.SettlementAccount
import services.BikParser
import telegram.entities.state.BankCollectorState
import telegram.resources.strings.CollectorStrings

@Suppress("LongMethod")
fun CollectorMapBuilder.bankInfoCollector() {
    collector<PaymentDetails>(initialState = BankCollectorState.WaitingForBik) {
        state<BankCollectorState.WaitingForBik> {
            val parser = BikParser()
            onEnter { sendTextMessage(it, CollectorStrings.Bank.Bik) }
            onText {
                val bik = Bik.of(it.content.text)
                if (bik != null) {
                    val bank = parser.parseWebPage(bik)
                    if (bank != null) {
                        state.override {
                            BankCollectorState.WaitingForSettlementAccount(bank)
                        }
                    } else {
                        state.override { BankCollectorState.HandsWaitingForCorrAccount(bik) }
                    }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.Bik)
                    return@onText
                }
            }
        }
        state<BankCollectorState.HandsWaitingForCorrAccount> {
            onEnter { sendTextMessage(it, CollectorStrings.Bank.CorrespondentAccount) }
            onText {
                val correspondentAccount = CorrespondentAccount.of(it.content.text)
                if (correspondentAccount != null) {
                    state.override { BankCollectorState.HandsWaitingForBankName(bik, correspondentAccount) }
                } else {
                    sendTextMessage(it.chat, CollectorStrings.Recommendations.CorrespondentAccount)
                    return@onText
                }
            }
        }
        state<BankCollectorState.HandsWaitingForBankName> {
            onEnter { sendTextMessage(it, CollectorStrings.Bank.BankName) }
            onText {
                state.override {
                    BankCollectorState.WaitingForSettlementAccount(
                        Bank(this.bik, this.correspondentAccount, it.content.text)
                    )
                }
            }
        }
        state<BankCollectorState.WaitingForSettlementAccount> {
            onEnter { sendTextMessage(it, CollectorStrings.Bank.SettlementAccount) }
            onText { message ->
                val settlementAccount = SettlementAccount.of(message.content.text)
                if (settlementAccount != null) {
                    val info = PaymentDetails(
                        bank = state.snapshot.mainInfo,
                        settlementAccount = settlementAccount
                    )
                    this@collector.exit(state, info)
                } else {
                    sendTextMessage(message.chat, CollectorStrings.Recommendations.SettlementAccount)
                    return@onText
                }
            }
        }
    }
}
