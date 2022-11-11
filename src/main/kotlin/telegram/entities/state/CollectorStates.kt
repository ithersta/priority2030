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

object BankCollectorState {
    @Serializable
    object WaitingForBik : DialogState

    @Serializable
    data class WaitingForPaymentAccount(val bik: String, val correspondentAccount: String, val bankName: String) :
        DialogState

    @Serializable
    data class HandsWaitingForCorrAccount(val bik: String) : DialogState

    @Serializable
    data class HandsWaitingForBankName(val bik: String, val correspondentAccount: String) : DialogState
}

@Serializable
object OrganizationTypeState : DialogState

object CompanyCollectorState {
    @Serializable
    object WaitingForInn : DialogState

    @Serializable
    data class WaitingForKpp(val inn: String) : DialogState
}

object IpCollectorState {
    @Serializable
    object WaitingForInn : DialogState
    @Serializable
    data class WaitingInspection(val inn: String , val fullNameOfOrg: String ): DialogState

    @Serializable
    data class WaitingResponse(val inn: String, val fullNameOfOrg: String) : DialogState

    @Serializable
    data class HandsWaitingOgrn(val inn: String) : DialogState

    @Serializable
    data class HandsWaitingOkpo(val inn: String, val orgn: String) : DialogState

    @Serializable
    data class HandsWaitingFullNameOfOrg(val inn: String, val orgn: String, val okpo: String) : DialogState

    @Serializable
    data class WaitingPhone(val inn: String, val orgn: String, val okpo: String, val fullNameOfOrg: String) :
        DialogState

    @Serializable
    data class WaitingEmail(
        val inn: String,
        val orgn: String,
        val okpo: String,
        val fullNameOfOrg: String,
        val phone: String
    ) : DialogState


}
