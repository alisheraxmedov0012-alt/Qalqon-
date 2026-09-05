package uz.qalqon.app.data.protection

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProtectionOverlayController {

    private val _isBlocked = MutableStateFlow(false)
    val isBlocked: StateFlow<Boolean> = _isBlocked

    private val _blockMessage = MutableStateFlow("Bu ilova vaqtincha bloklangan")
    val blockMessage: StateFlow<String> = _blockMessage

    fun showBlock(message: String) {
        _blockMessage.value = message
        _isBlocked.value = true
    }

    fun hideBlock() {
        _isBlocked.value = false
    }
}
