package telegram.collectors

import com.ithersta.tgbotapi.fsm.builders.NestedStateMachineBuilder
import com.ithersta.tgbotapi.fsm.builders.StateFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.StateMachine
import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import dev.inmo.tgbotapi.types.UserId
import domain.datatypes.FieldData
import telegram.entities.state.CollectingDataState
import telegram.entities.state.DialogState
import kotlin.reflect.KClass

private typealias CollectorNestedStateMachineBuilder<T> =
        NestedStateMachineBuilder<DialogState, *, CollectingDataState, *, UserId, CollectorResult<T>>

sealed interface CollectorResult<T : FieldData> {
    class OK<T : FieldData>(val data: T) : CollectorResult<T>
    object Back : CollectorResult<FieldData>
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
                    is CollectorResult.Back -> copy(fieldsData = fieldsData.dropLast(1))
                    is CollectorResult.OK -> copy(fieldsData = fieldsData + result.data)
                }
            }
        ) {
            anyState {
                onCommand("back", description = null) {
                    this@nestedStateMachine.exit(state, CollectorResult.Back)
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
