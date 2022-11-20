package telegram.collectors

import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import domain.datatypes.PurchaseCost
import telegram.entities.state.PurchaseCostState
import telegram.resources.strings.CollectorStrings
import ru.morpher.ws3.ClientBuilder


fun CollectorMapBuilder.purchaseCostCollector() {
    collector<PurchaseCost>(initialState = PurchaseCostState) {
        state<PurchaseCostState> {
            val morpherToken = System.getenv("MORPHER_TOKEN")
            val client = ClientBuilder()
                .useToken(morpherToken)
                .build()
            /// ну тут видимо проверка, что ещё можно просить деньги..........
            ///если деньги уже нельзя просить, до заставить всё пользователя самого вводить..........
            onEnter {
                sendTextMessage(
                    it,
                    CollectorStrings.PurchaseCost
                )
            }
            onText {
                ////ну тут потом ещё проверки
                val totalCost = it.content.text
                val rubles=totalCost.substringBefore('.').toInt()
                val cops=totalCost.substringAfter('.').toInt()

                val purchaseRub=client.russian().spell(rubles,"рубль")
                val rublesRu=purchaseRub.numberDeclension.nominative
                val rubl=purchaseRub.unitDeclension.nominative

                val purchaseCop=client.russian().spell(cops,"копейка")
                val copsRu=purchaseCop.numberDeclension.nominative
                val cop=purchaseCop.unitDeclension.nominative

                val purchaseCost =PurchaseCost(
                    costInRubles = rubles.toString(),
                    costInCops = cops.toString(),
                    costInRublesPrescription = rublesRu,
                    costInCopsPrescription = copsRu,
                    rubles = rubl,
                    cops=cop
                )
                this@collector.exit(state, listOf(purchaseCost))
            }
        }
    }
}
