package telegram.entities.state

import domain.datatypes.BankInfo
import domain.datatypes.IpInfo
import domain.datatypes.OrgInfo
import domain.entities.Fio
import domain.entities.PhoneNumber
import domain.entities.SelectionIdentifier
import domain.entities.SelectionLetter
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

object CostCollectorState {
    @Serializable
    object WaitingPrice : DialogState
}

object FullNameCollectorState {
    @Serializable
    object WaitingForLastName : DialogState

    @Serializable
    data class WaitingForFirstName(
        val lastName: String
    ) : DialogState

    @Serializable
    data class WaitingForPatronymic(
        val lastName: String,
        val firstName: String
    ) : DialogState
}

object BankCollectorState {
    @Serializable
    object WaitingForBik : DialogState

    @Serializable
    data class WaitingForPaymentAccount(
        val mainInfo: BankInfo,
    ) : DialogState

    @Serializable
    data class HandsWaitingForCorrAccount(
        val bik: String
    ) : DialogState

    @Serializable
    data class HandsWaitingForBankName(
        val bik: String,
        val correspondentAccount: String
    ) : DialogState
}

@Serializable
object OrganizationTypeState : DialogState

object CompanyCollectorState {
    @Serializable
    object WaitingForInn : DialogState

    @Serializable
    data class WaitingForKpp(
        val inn: String
    ) : DialogState

    @Serializable
    data class WaitingInspection(
        val mainInfo: OrgInfo,
        val fullNameOfOrg: String
    ) : DialogState

    @Serializable
    data class HandsWaitingOgrn(
        val inn: String,
        val kpp: String
    ) : DialogState


    @Serializable
    data class HandsWaitingFullNameOfOrg(
        val inn: String,
        val kpp: String,
        val ogrn: String,
    ) : DialogState

    @Serializable
    data class HandsWaitingFullNameOfHolder(
        val inn: String,
        val kpp: String,
        val ogrn: String,
        val fullNameOfOrg: String
    ) : DialogState

    @Serializable
    data class HandsWaitingPost(
        val inn: String,
        val kpp: String,
        val ogrn: String,
        val fullNameOfOrg: String,
        val fullNameOfHolder: String
    ) : DialogState

    @Serializable
    data class HandsWaitingLocation(
        val inn: String,
        val kpp: String,
        val ogrn: String,
        val fullNameOfOrg: String,
        val fullNameOfHolder: String,
        val post: String
    ) : DialogState

    @Serializable
    data class WaitingPhone(
        val mainInfo: OrgInfo
    ) : DialogState

    @Serializable
    data class WaitingEmail(
        val mainInfo: OrgInfo,
        val phone: String
    ) : DialogState
}

object IpCollectorState {
    @Serializable
    object WaitingForInn : DialogState

    @Serializable
    data class WaitingInspection(
        val mainInfo: IpInfo,
        val fullNameOfHolder: String
    ) : DialogState

    @Serializable
    data class HandsWaitingOgrn(
        val inn: String
    ) : DialogState

    @Serializable
    data class HandsWaitingDataOfOgrn(
        val inn: String,
        val ogrn: String,
    ) : DialogState

    @Serializable
    data class HandsWaitingfullNameOfHolder(
        val inn: String,
        val ogrn: String,
        val dataOgrn: String
    ) : DialogState

    @Serializable
    data class HandsWaitingLocation(
        val inn: String,
        val ogrn: String,
        val dataOgrn: String,
        val fullNameOfHolder: String
    ) : DialogState

    @Serializable
    data class WaitingPhone(
        val mainInfo: IpInfo
    ) : DialogState

    @Serializable
    data class WaitingEmail(
        val mainInfo: IpInfo,
        val phone: String
    ) : DialogState
}

object PurchaseDescriptionState {
    @Serializable
    object WaitingForShortJustification : DialogState

    @Serializable
    data class WaitingForSelectionLetter(val shortJustification: String) : DialogState

    @Serializable
    data class WaitingForSelectionIdentifier(
        val shortJustification: String,
        val selectionLetter: SelectionLetter
    ) : DialogState

    @Serializable
    data class WaitingForFullJustification(
        val shortJustification: String,
        val selectionLetter: SelectionLetter,
        val selectionIdentifier: SelectionIdentifier
    ) : DialogState

    @Serializable
    data class WaitingForMaterialValuesNeed(
        val shortJustification: String,
        val selectionLetter: SelectionLetter,
        val selectionIdentifier: SelectionIdentifier,
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
    object WaitingForFio : DialogState

    @Serializable
    data class WaitingForContactPhoneNumber(val fio: Fio) : DialogState
}

object ResponsibleForDocumentsPersonState {
    @Serializable
    object WaitingForFio : DialogState

    @Serializable
    data class WaitingForContactPhoneNumber(val fio: Fio) : DialogState

    @Serializable
    data class WaitingForEmail(
        val fio: Fio, val contactPhoneNumber: PhoneNumber
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
    data class WaitingForDeliveryAddress(val deadline: LocalDate) : DialogState
}

@Serializable
object MaterialObjectNumberState : DialogState

@Serializable
object TermOfPaymentState : DialogState
