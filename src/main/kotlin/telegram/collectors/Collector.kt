package telegram.collectors

import com.ithersta.tgbotapi.fsm.builders.NestedStateMachineBuilder
import com.ithersta.tgbotapi.fsm.builders.StateFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.StateMachine
import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import com.ithersta.tgbotapi.fsm.entities.triggers.onEnter
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.utils.row
import domain.datatypes.FieldData
import telegram.entities.state.CancelCollectingDataState
import telegram.entities.state.CollectingDataState
import telegram.entities.state.DialogState
import telegram.entities.state.EmptyState
import telegram.flows.backCommand
import telegram.resources.strings.ButtonStrings
import telegram.resources.strings.InvalidInputStrings
import telegram.resources.strings.Strings
import kotlin.reflect.KClass

private typealias CollectorNestedStateMachineBuilder<T> =
NestedStateMachineBuilder<DialogState, *, CollectingDataState, *, UserId, CollectorResult<T>>

sealed interface CollectorResult<T : FieldData> {
    class OK<T : FieldData>(val data: T) : CollectorResult<T>
    object Cancel : CollectorResult<FieldData>
}

@DslMarker
annotation class CollectorDsl

@CollectorDsl
class CollectorMapBuilder {
    val blocks = mutableListOf<CollectorNestedStateMachineBuilder<*>.() -> Unit>()
    val map = mutableMapOf<KClass<out FieldData>, DialogState>()

    inline fun <reified T : FieldData> collector(
        initialState: DialogState,
        noinline block: CollectorNestedStateMachineBuilder<T>.() -> Unit
    ) {
        blocks += block as CollectorNestedStateMachineBuilder<*>.() -> Unit
        map[T::class] = initialState
    }

    fun build(
        stateFilterBuilder: StateFilterBuilder<DialogState, *, CollectingDataState, *, UserId>
    ): Map<KClass<out FieldData>, DialogState> {
        val missingCollectors = FieldData::class.sealedSubclasses - map.keys
        check(missingCollectors.isEmpty()) {
            "Missing collectors for: ${missingCollectors.joinToString()}"
        }
        stateFilterBuilder.nestedStateMachine(
            onExit = { result ->
                when (result) {
                    is CollectorResult.OK -> copy(fieldsData = fieldsData + result.data)
                    is CollectorResult.Cancel -> EmptyState
                }
            }
        ) {
            state<CancelCollectingDataState> {
                onEnter {
                    sendTextMessage(
                        it,
                        Strings.CancelDataCollection,
                        replyMarkup = replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                            row {
                                simpleButton(ButtonStrings.No)
                                simpleButton(ButtonStrings.Yes)
                            }
                        }
                    )
                }
                onText(ButtonStrings.No) { state.override { returnTo } }
                onText(ButtonStrings.Yes) { this@nestedStateMachine.exit(state, CollectorResult.Cancel) }
                onText { sendTextMessage(it.chat, InvalidInputStrings.InvalidAnswer) }
            }
            anyState {
                backCommand()
                onCommand("cancel", description = null) {
                    state.override { CancelCollectingDataState(this) }
                }
            }
            blocks.forEach { it() }
        }
        return map
    }
}

suspend fun <T : FieldData> CollectorNestedStateMachineBuilder<T>.exit(
    state: StateMachine<DialogState, *, *>.StateHolder<*>,
    data: T
) {
    exit(state, CollectorResult.OK(data))
}

fun StateFilterBuilder<DialogState, *, CollectingDataState, *, UserId>.buildCollectorMap(
    block: CollectorMapBuilder.() -> Unit
) = CollectorMapBuilder().apply(block).build(this)
