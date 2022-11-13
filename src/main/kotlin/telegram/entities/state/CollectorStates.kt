package telegram.entities.state

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
        val bik: String,
        val correspondentAccount: String,
        val bankName: String
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
        val inn: String,
        val fullNameOfOrg: String
    ) : DialogState

    @Serializable
    data class HandsWaitingOgrn(
        val inn: String,
        val kpp: String
    ) : DialogState

    @Serializable
    data class HandsWaitingOkpo(
        val inn: String,
        val kpp: String,
        val ogrn: String
    ) : DialogState

    @Serializable
    data class HandsWaitingFullNameOfOrg(
        val inn: String,
        val kpp: String,
        val ogrn: String,
        val okpo: String
    ) : DialogState

    @Serializable
    data class HandsWaitingFullNameOfHolder(
        val inn: String,
        val kpp: String,
        val ogrn: String,
        val okpo: String,
        val fullNameOfOrg: String
    ) : DialogState

    @Serializable
    data class HandsWaitingPost(
        val inn: String,
        val kpp: String,
        val ogrn: String,
        val okpo: String,
        val fullNameOfOrg: String,
        val fullNameOfHolder: String
    ) : DialogState

    @Serializable
    data class HandsWaitingLocation(
        val inn: String,
        val kpp: String,
        val ogrn: String,
        val okpo: String,
        val fullNameOfOrg: String,
        val fullNameOfHolder: String,
        val post: String
    ) : DialogState

    @Serializable
    data class WaitingPhone(
        val inn: String,
        val kpp: String,
        val ogrn: String,
        val okpo: String,
        val fullNameOfOrg: String,
        val fullNameOfHolder: String,
        val post: String,
        val location: String
    ) : DialogState

    @Serializable
    data class WaitingEmail(
        val inn: String,
        val kpp: String,
        val ogrn: String,
        val okpo: String,
        val fullNameOfOrg: String,
        val fullNameOfHolder: String,
        val location: String,
        val post: String,
        val phone: String
    ) : DialogState
}

object IpCollectorState {
    @Serializable
    object WaitingForInn : DialogState

    @Serializable
    data class WaitingInspection(
        val inn: String,
        val fullNameOfOrg: String
    ) : DialogState

    @Serializable
    data class HandsWaitingOgrn(
        val inn: String
    ) : DialogState

    @Serializable
    data class HandsWaitingOkpo(
        val inn: String,
        val ogrn: String
    ) : DialogState

    @Serializable
    data class HandsWaitingDataOfOgrn(
        val inn: String,
        val ogrn: String,
        val okpo: String
    ) : DialogState

    @Serializable
    data class HandsWaitingFullNameOfOrg(
        val inn: String,
        val ogrn: String,
        val okpo: String,
        val dataOgrn: String
    ) : DialogState

    @Serializable
    data class WaitingPhone(
        val inn: String,
        val ogrn: String,
        val okpo: String,
        val fullNameOfOrg: String,
        val dataOkpo: String
    ) : DialogState

    @Serializable
    data class WaitingEmail(
        val inn: String,
        val ogrn: String,
        val okpo: String,
        val fullNameOfOrg: String,
        val dataOkpo: String,
        val phone: String
    ) : DialogState
}
