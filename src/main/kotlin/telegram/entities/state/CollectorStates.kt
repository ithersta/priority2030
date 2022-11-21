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


object PurchaseCostState {
    @Serializable
    object MorpherState : DialogState

    @Serializable
    object WaitingForCostInRubles : DialogState

    @Serializable
    data class WaitingForRublesPrescription(val costInRubles: String) : DialogState

    @Serializable
    data class WaitingForCostInCops(
        val costInRubles: String,
        val rublesPrescription: String
    ) : DialogState

    @Serializable
    data class WaitingForCopsPrescription(
        val costInRubles: String,
        val rublesPrescription: String,
        val costInCops: String
    ) : DialogState

    @Serializable
    data class WaitingForRubles(
        val costInRubles: String,
        val rublesPrescription: String,
        val costInCops: String,
        val copsPrescription: String
    ) : DialogState

    @Serializable
    data class WaitingForCops(
        val costInRubles: String,
        val rublesPrescription: String,
        val costInCops: String,
        val copsPrescription: String,
        val rubles:String
    ) : DialogState
}


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
