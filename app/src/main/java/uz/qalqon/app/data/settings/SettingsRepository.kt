package uz.qalqon.app.data.settings

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "app_settings")

class SettingsRepository(private val context: Context) {

    companion object {
        private val PROTECTION_ENABLED = booleanPreferencesKey("protection_enabled")
        private val SCAN_MODE = stringPreferencesKey("scan_mode")
        private val UNKNOWN_POLICY = stringPreferencesKey("unknown_policy")
        private val NO_FACE_POLICY = stringPreferencesKey("no_face_policy")
        private val RECOVERY_DELAY = intPreferencesKey("recovery_delay")
        private val LOW_BATTERY_MODE = booleanPreferencesKey("low_battery_mode")
    }

    val settingsFlow: Flow<AppSettings> = context.settingsDataStore.data.map { prefs ->
        AppSettings(
            protectionEnabled = prefs[PROTECTION_ENABLED] ?: false,
            scanMode = prefs[SCAN_MODE] ?: "balanced",
            unknownUserPolicy = prefs[UNKNOWN_POLICY] ?: "allow",
            noFacePolicy = prefs[NO_FACE_POLICY] ?: "soft_block",
            recoveryDelaySeconds = prefs[RECOVERY_DELAY] ?: 5,
            lowBatteryModeEnabled = prefs[LOW_BATTERY_MODE] ?: true
        )
    }

    suspend fun setProtectionEnabled(value: Boolean) {
        context.settingsDataStore.edit { it[PROTECTION_ENABLED] = value }
    }

    suspend fun setScanMode(value: String) {
        context.settingsDataStore.edit { it[SCAN_MODE] = value }
    }

    suspend fun setUnknownPolicy(value: String) {
        context.settingsDataStore.edit { it[UNKNOWN_POLICY] = value }
    }

    suspend fun setNoFacePolicy(value: String) {
        context.settingsDataStore.edit { it[NO_FACE_POLICY] = value }
    }

    suspend fun setRecoveryDelay(value: Int) {
        context.settingsDataStore.edit { it[RECOVERY_DELAY] = value }
    }

    suspend fun setLowBatteryModeEnabled(value: Boolean) {
        context.settingsDataStore.edit { it[LOW_BATTERY_MODE] = value }
    }
}

