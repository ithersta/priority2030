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
    object WaitingForShortJustification : DialogState

    @Serializable
    data class WaitingForSelectionLetter(val shortJustification: String) : DialogState

    @Serializable
    data class WaitingForSelectionIdentifier(
        val shortJustification: String,
        val selectionLetter: String
    ) : DialogState

    @Serializable
    data class WaitingForFullJustification(
        val shortJustification: String,
        val selectionLetter: String,
        val selectionIdentifier: String
    ) : DialogState

    @Serializable
    data class WaitingForMaterialValuesNeed(
        val shortJustification: String,
        val selectionLetter: String,
        val selectionIdentifier: String,
        val fullJustification: String
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
    object WaitingForfio : DialogState

    @Serializable
    data class WaitingForContactPhoneNumber(val fio: String) : DialogState
}

object ResponsibleForDocumentsPersonState {
    @Serializable
    object WaitingForfio : DialogState

    @Serializable
    data class WaitingForContactPhoneNumber(val fio: String) : DialogState

    @Serializable
    data class WaitingForEmail(
        val fio: String, val contactPhoneNumber: String
    ) : DialogState
}

@Serializable
object PurchaseObjectState : DialogState

@Serializable
object PurchaseInitiatorDepartmentState : DialogState

object PurchaseDeadlineAndDeliveryAddressState {
    @Serializable
    object WaitingForDeadline : DialogState

    @Serializable
    data class WaitingForDeliveryAddress(val deadline: String) : DialogState
}

@Serializable
object MaterialObjectNumberState : DialogState

@Serializable
object TermOfPaymentState : DialogState
