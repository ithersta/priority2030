package telegram.entities.state

import domain.datatypes.Bank
import domain.entities.IpInfo
import domain.entities.OrgInfo
import domain.entities.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

object BankCollectorState {
    @Serializable
    object WaitingForBik : DialogState

    @Serializable
    data class WaitingForSettlementAccount(
        val mainInfo: Bank,
    ) : DialogState

    @Serializable
    data class HandsWaitingForCorrAccount(
        val bik: Bik
    ) : DialogState

    @Serializable
    data class HandsWaitingForBankName(
        val bik: Bik,
        val correspondentAccount: CorrespondentAccount
    ) : DialogState
}

@Serializable
object OrganizationTypeState : DialogState

object CompanyCollectorState {
    @Serializable
    object WaitingForInn : DialogState

    @Serializable
    data class WaitingForKpp(
        val inn: OooInn
    ) : DialogState

    @Serializable
    data class WaitingInspection(
        val mainInfo: OrgInfo,
        val fullNameOfOrg: String
    ) : DialogState

    @Serializable
    data class HandsWaitingOgrn(
        val inn: OooInn,
        val kpp: Kpp
    ) : DialogState

    @Serializable
    data class HandsWaitingFullNameOfOrg(
        val inn: OooInn,
        val kpp: Kpp,
        val ogrn: OooOgrn,
    ) : DialogState

    @Serializable
    data class HandsWaitingFullNameOfHolder(
        val inn: OooInn,
        val kpp: Kpp,
        val ogrn: OooOgrn,
        val fullNameOfOrg: String
    ) : DialogState

    @Serializable
    data class HandsWaitingPost(
        val inn: OooInn,
        val kpp: Kpp,
        val ogrn: OooOgrn,
        val fullNameOfOrg: String,
        val fullNameOfHolder: String
    ) : DialogState

    @Serializable
    data class HandsWaitingLocation(
        val inn: OooInn,
        val kpp: Kpp,
        val ogrn: OooOgrn,
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
        val phone: PhoneNumber
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
        val inn: IpInn
    ) : DialogState

    @Serializable
    data class HandsWaitingDataOfOgrn(
        val inn: IpInn,
        val ogrn: IpOgrn,
    ) : DialogState

    @Serializable
    data class HandsWaitingfullNameOfHolder(
        val inn: IpInn,
        val ogrn: IpOgrn,
        val dataOgrn: String
    ) : DialogState

    @Serializable
    data class HandsWaitingLocation(
        val inn: IpInn,
        val ogrn: IpOgrn,
        val dataOgrn: String,
        val fullNameOfHolder: String
    ) : DialogState

    @Serializable
    data class  WaitingSpecifyLegalAddressOfEntrepreneur(
        val mainInfo: IpInfo
    ):DialogState

    @Serializable
    data class WaitingPhone(
        val mainInfo: IpInfo
    ) : DialogState

    @Serializable
    data class WaitingEmail(
        val mainInfo: IpInfo,
        val phone: PhoneNumber
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
