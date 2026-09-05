package uz.qalqon.app.data.protection

sealed class ProtectionState {
    data object Unprotected : ProtectionState()
    data object SoftBlocked : ProtectionState()
    data object HardBlocked : ProtectionState()
    data object Recovering : ProtectionState()
}
