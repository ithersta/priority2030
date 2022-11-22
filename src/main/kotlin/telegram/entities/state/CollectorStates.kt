package telegram.entities.state

import domain.datatypes.BankInfo
import domain.datatypes.IpInfo
import domain.datatypes.OrgInfo
import kotlinx.serialization.Serializable

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
    data class HandsWaitingLocation(
        val inn: String,
        val ogrn: String,
        val dataOgrn: String,
        val fullNameOfHolder :String
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
