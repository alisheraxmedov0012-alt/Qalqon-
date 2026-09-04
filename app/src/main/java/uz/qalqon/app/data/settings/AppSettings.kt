package uz.qalqon.app.data.settings

data class AppSettings(
    val protectionEnabled: Boolean = false,
    val scanMode: String = "balanced",
    val unknownUserPolicy: String = "allow",
    val noFacePolicy: String = "soft_block",
    val recoveryDelaySeconds: Int = 5,
    val lowBatteryModeEnabled: Boolean = true
)

