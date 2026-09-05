package uz.qalqon.app.data.protection

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uz.qalqon.app.data.recognition.RecognitionResult
import uz.qalqon.app.data.settings.AppSettings

class ProtectionDebugEngine {

    private val _state = MutableStateFlow<ProtectionState>(ProtectionState.Unprotected)
    val state: StateFlow<ProtectionState> = _state

    fun evaluate(
        recognitionResult: RecognitionResult,
        settings: AppSettings
    ) {
        if (!settings.protectionEnabled) {
            _state.value = ProtectionState.Unprotected
            return
        }

        _state.value = when (recognitionResult) {
            is RecognitionResult.ParentRecognized -> {
                ProtectionState.Unprotected
            }

            is RecognitionResult.ChildRecognized -> {
                when (settings.unknownUserPolicy) {
                    "soft_block" -> ProtectionState.SoftBlocked
                    "allow" -> ProtectionState.SoftBlocked
                    else -> ProtectionState.HardBlocked
                }
            }

            is RecognitionResult.Unknown -> {
                when (settings.unknownUserPolicy) {
                    "allow" -> ProtectionState.Unprotected
                    "soft_block" -> ProtectionState.SoftBlocked
                    else -> ProtectionState.HardBlocked
                }
            }

            is RecognitionResult.NoFace -> {
                when (settings.noFacePolicy) {
                    "allow" -> ProtectionState.Unprotected
                    "soft_block" -> ProtectionState.SoftBlocked
                    else -> ProtectionState.HardBlocked
                }
            }
        }
    }

    fun setRecovering() {
        _state.value = ProtectionState.Recovering
    }

    fun reset() {
        _state.value = ProtectionState.Unprotected
    }
}
