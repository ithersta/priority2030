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

private typealias CollectorNestedStateMachineBuilder =
        NestedStateMachineBuilder<DialogState, *, CollectingDataState, *, UserId, CollectorResult>

sealed interface CollectorResult {
    class OK(val data: List<FieldData>) : CollectorResult
    object Back : CollectorResult
}

@DslMarker
annotation class CollectorDsl

@CollectorDsl
class CollectorMapBuilder {
    val blocks = mutableListOf<CollectorNestedStateMachineBuilder.() -> Unit>()
    val map = mutableMapOf<KClass<out FieldData>, DialogState>()

    inline fun <reified T : FieldData> collector(
        initialState: DialogState,
        noinline block: CollectorNestedStateMachineBuilder.() -> Unit
    ) {
        blocks += block
        map[T::class] = initialState
    }

    fun build(
        stateFilterBuilder: StateFilterBuilder<DialogState, *, CollectingDataState, *, UserId>
    ): Map<KClass<out FieldData>, DialogState> {
        val missingCollectors = FieldData::class.sealedSubclasses - map.keys
        check(missingCollectors.isEmpty()) {
            "Missing collectors for: ${missingCollectors.joinToString()}"
        }
        stateFilterBuilder.nestedStateMachine<CollectorResult>(
            onExit = { result ->
                when (result) {
                    CollectorResult.Back -> copy(fieldsData = fieldsData.dropLast(1))
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

suspend fun CollectorNestedStateMachineBuilder.exit(
    state: StateMachine<DialogState, *, *>.StateHolder<*>,
    vararg data: FieldData
) {
    exit(state, CollectorResult.OK(data.toList()))
}

fun StateFilterBuilder<DialogState, *, CollectingDataState, *, UserId>.buildCollectorMap(
    block: CollectorMapBuilder.() -> Unit
) = CollectorMapBuilder().apply(block).build(this)
