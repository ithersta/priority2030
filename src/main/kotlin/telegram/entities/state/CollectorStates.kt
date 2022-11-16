package telegram.entities.state

import kotlinx.serialization.Serializable

object FullNameCollectorState {
    @Serializable
    object WaitingForLastName : DialogState

    @Serializable
    data class WaitingForFirstName(val lastName: String) : DialogState

    @Serializable
    data class WaitingForPatronymic(val lastName: String, val firstName: String) : DialogState
}

@Serializable
object OrganizationTypeState : DialogState

object PurchaseDescriptionState {
    @Serializable
    object WaitingForShortName : DialogState

    @Serializable
    data class WaitingForShortJustification(val shortName: String) : DialogState

    @Serializable
    data class WaitingForSelectionLetter(val shortName: String, val shortJustification: String) : DialogState

    @Serializable
    data class WaitingForSelectionIdentifier(
        val shortName: String,
        val shortJustification: String,
        val selectionLetter:String
    ) : DialogState

    @Serializable
    data class WaitingForFullJustification(
        val shortName: String,
        val shortJustification: String,
        val selectionLetter:String,
        val selectionIdentifier:String
    ) : DialogState
}

@Serializable
object PurchasePointState : DialogState

@Serializable
object PurchaseIniciatorState : DialogState

@Serializable
object PurchaseCostState : DialogState

object FinanciallyResponsiblePersonState {
    @Serializable
    object WaitingForFIO : DialogState

    @Serializable
    data class WaitingForContactPhoneNumber(val FIO: String) : DialogState

    @Serializable
    data class WaitingForWorkPhoneNumber(val FIO: String, val contactPhoneNumber: String) : DialogState
}

object ResponsibleForDocumentsPersonState {
    @Serializable
    object WaitingForFIO : DialogState

    @Serializable
    data class WaitingForContactPhoneNumber(val FIO: String) : DialogState

    @Serializable
    data class WaitingForWorkPhoneNumber(val FIO: String, val contactPhoneNumber: String) : DialogState

    @Serializable
    data class WaitingForEmail(
        val FIO: String,
        val contactPhoneNumber: String,
        val workPhoneNumber:String
    ) : DialogState
}