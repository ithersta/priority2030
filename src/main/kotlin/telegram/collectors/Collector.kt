package telegram.collectors

import com.ithersta.tgbotapi.fsm.builders.NestedStateMachineBuilder
import com.ithersta.tgbotapi.fsm.builders.StateFilterBuilder
import dev.inmo.tgbotapi.types.UserId
import domain.datatypes.FieldData
import telegram.entities.state.CollectingDataState
import telegram.entities.state.DialogState
import kotlin.reflect.KClass

private typealias CollectorNestedStateMachineBuilder =
        NestedStateMachineBuilder<DialogState, *, CollectingDataState, *, UserId, List<FieldData>>

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
        stateFilterBuilder.nestedStateMachine(
            onExit = { copy(fieldsData = fieldsData + it) }
        ) {
            blocks.forEach { it() }
        }
        return map
    }
}

fun StateFilterBuilder<DialogState, *, CollectingDataState, *, UserId>.buildCollectorMap(
    block: CollectorMapBuilder.() -> Unit
) = CollectorMapBuilder().apply(block).build(this)
