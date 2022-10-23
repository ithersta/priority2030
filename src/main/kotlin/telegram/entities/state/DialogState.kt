package telegram.entities.state

import kotlinx.serialization.Serializable

@Serializable
sealed interface DialogState

@Serializable
object EmptyState : DialogState
