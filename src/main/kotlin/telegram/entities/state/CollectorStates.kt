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
    data class handsWaitingForCorrAccount(val bik: String) : DialogState

    @Serializable
    data class handsWaitingForBankName(val bik: String, val correspondentAccount: String) : DialogState
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
    data class WaitingForInfo(val inn: String) : DialogState

}
