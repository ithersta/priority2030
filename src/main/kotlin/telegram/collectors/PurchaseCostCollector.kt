package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.PurchaseCost
import telegram.entities.state.PurchaseCostState
import telegram.resources.strings.CollectorStrings
import ru.morpher.ws3.ClientBuilder
import telegram.resources.strings.InvalidInputStrings
import telegram.resources.strings.InvalidInputStrings.InvalidPurchaseCost
import validation.IsNumberValid
import validation.IsPurchaseCostValid


fun CollectorMapBuilder.purchaseCostCollector() {
    collector<PurchaseCost>(initialState = PurchaseCostState.MorpherState) {
        state<PurchaseCostState.MorpherState> {
            val morpherToken = System.getenv("MORPHER_TOKEN")
            val client = ClientBuilder()
                .useToken(morpherToken)
                .build()
            onEnter {
                if (client.queriesLeftForToday() > 0) {
                    sendTextMessage(
                        it,
                        CollectorStrings.PurchaseCost.Morpher
                    )
                } else {
                    state.override { PurchaseCostState.WaitingForCostInRubles }
                }
            }

            onText {
                val totalCost = it.content.text
                if (IsPurchaseCostValid(totalCost)) {
                    val rubles = totalCost.substringBefore('.').toInt()
                    val cops = totalCost.substringAfter('.').toInt()

                    val purchaseRub = client.russian().spell(rubles, "рубль")
                    val rublesRu = purchaseRub.numberDeclension.nominative
                    val rubl = purchaseRub.unitDeclension.nominative

                    val purchaseCop = client.russian().spell(cops, "копейка")
                    val copsRu = purchaseCop.numberDeclension.nominative
                    val cop = purchaseCop.unitDeclension.nominative

                    val purchaseCost = PurchaseCost(
                        costInRubles = rubles.toString(),
                        costInCops = cops.toString(),
                        costInRublesPrescription = rublesRu,
                        costInCopsPrescription = copsRu,
                        rubles = rubl,
                        cops = cop
                    )
                    this@collector.exit(state, listOf(purchaseCost))
                } else {
                    sendTextMessage(it.chat, InvalidPurchaseCost)
                }
            }
        }
        state<PurchaseCostState.WaitingForCostInRubles> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseCost.CostInRubles) }
            onText {
                val costInRubles = it.content.text
                if (IsNumberValid(costInRubles)) {
                    state.override { PurchaseCostState.WaitingForRublesPrescription(costInRubles) }
                } else {
                    sendTextMessage(it.chat, InvalidInputStrings.InvalidNumber)
                }
            }
        }

        state<PurchaseCostState.WaitingForRublesPrescription> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseCost.CostInRublesPrescription) }
            onText {
                state.override { PurchaseCostState.WaitingForCostInCops(this.costInRubles, it.content.text) }
            }
        }

        state<PurchaseCostState.WaitingForCostInCops> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseCost.CostInCops) }
            onText {
                val costInCops = it.content.text
                if (IsNumberValid(costInCops)) {
                    state.override {
                        PurchaseCostState.WaitingForCopsPrescription(
                            this.costInRubles,
                            this.rublesPrescription,
                            costInCops
                        )
                    }
                } else {
                    sendTextMessage(it.chat, InvalidInputStrings.InvalidNumber)
                }
            }
        }

        state<PurchaseCostState.WaitingForCopsPrescription> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseCost.CostInCopsPrescription) }
            onText {
                state.override {
                    PurchaseCostState.WaitingForRubles(
                        this.costInRubles,
                        this.rublesPrescription,
                        this.costInCops,
                        it.content.text
                    )
                }
            }
        }

        state<PurchaseCostState.WaitingForRubles> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseCost.Rubles) }
            onText {
                state.override {
                    PurchaseCostState.WaitingForCops(
                        this.costInRubles,
                        this.rublesPrescription,
                        this.costInCops,
                        this.copsPrescription,
                        it.content.text
                    )
                }
            }
        }

        state<PurchaseCostState.WaitingForCops> {
            onEnter { sendTextMessage(it, CollectorStrings.PurchaseCost.Cops) }
            onText {

                val purchaseCost = PurchaseCost(
                    costInRubles = state.snapshot.costInRubles,
                    costInCops = state.snapshot.costInCops,
                    costInRublesPrescription = state.snapshot.rublesPrescription,
                    costInCopsPrescription = state.snapshot.copsPrescription,
                    rubles = state.snapshot.rubles,
                    cops = it.content.text
                )
                this@collector.exit(state, listOf(purchaseCost))
            }
        }
    }
}
