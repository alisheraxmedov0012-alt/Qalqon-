package uz.qalqon.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uz.qalqon.app.R
import uz.qalqon.app.data.repository.AppResetRepository
import uz.qalqon.app.data.settings.AppSettings
import uz.qalqon.app.data.settings.SettingsRepository

@Composable
fun SettingsScreen(
    settingsRepository: SettingsRepository,
    appResetRepository: AppResetRepository,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val settings by settingsRepository.settingsFlow.collectAsState(initial = AppSettings())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.settings_protection))
            Switch(
                checked = settings.protectionEnabled,
                onCheckedChange = {
                    scope.launch { settingsRepository.setProtectionEnabled(it) }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "${stringResource(R.string.settings_scan_mode)}: ${scanModeLabel(settings.scanMode)}")

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { scope.launch { settingsRepository.setScanMode("battery_saver") } }) {
                Text(text = stringResource(R.string.scan_battery))
            }
            Button(onClick = { scope.launch { settingsRepository.setScanMode("balanced") } }) {
                Text(text = stringResource(R.string.scan_balanced))
            }
            Button(onClick = { scope.launch { settingsRepository.setScanMode("strict") } }) {
                Text(text = stringResource(R.string.scan_strict))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "${stringResource(R.string.settings_unknown_policy)}: ${policyLabel(settings.unknownUserPolicy)}")

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { scope.launch { settingsRepository.setUnknownPolicy("allow") } }) {
                Text(text = stringResource(R.string.policy_allow))
            }
            Button(onClick = { scope.launch { settingsRepository.setUnknownPolicy("soft_block") } }) {
                Text(text = stringResource(R.string.policy_soft))
            }
            Button(onClick = { scope.launch { settingsRepository.setUnknownPolicy("hard_block") } }) {
                Text(text = stringResource(R.string.policy_hard))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "${stringResource(R.string.settings_no_face_policy)}: ${policyLabel(settings.noFacePolicy)}")

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { scope.launch { settingsRepository.setNoFacePolicy("allow") } }) {
                Text(text = stringResource(R.string.policy_allow))
            }
            Button(onClick = { scope.launch { settingsRepository.setNoFacePolicy("soft_block") } }) {
                Text(text = stringResource(R.string.policy_soft))
            }
            Button(onClick = { scope.launch { settingsRepository.setNoFacePolicy("hard_block") } }) {
                Text(text = stringResource(R.string.policy_hard))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "${stringResource(R.string.settings_recovery_delay)}: ${settings.recoveryDelaySeconds} s")

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { scope.launch { settingsRepository.setRecoveryDelay(3) } }) {
                Text("3s")
            }
            Button(onClick = { scope.launch { settingsRepository.setRecoveryDelay(5) } }) {
                Text("5s")
            }
            Button(onClick = { scope.launch { settingsRepository.setRecoveryDelay(10) } }) {
                Text("10s")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.settings_low_battery))
            Switch(
                checked = settings.lowBatteryModeEnabled,
                onCheckedChange = {
                    scope.launch { settingsRepository.setLowBatteryModeEnabled(it) }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch {
                    appResetRepository.clearActivityLogs()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.settings_clear_logs))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                scope.launch {
                    appResetRepository.resetAll()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.settings_reset_all))
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = onBackClick) {
            Text(text = stringResource(R.string.btn_back))
        }
    }
}

private fun scanModeLabel(mode: String): String {
    return when (mode) {
        "battery_saver" -> "Batareyani tejash"
        "strict" -> "Qattiq"
        else -> "Muvozanatli"
    }
}

private fun policyLabel(policy: String): String {
    return when (policy) {
        "soft_block" -> "Yumshoq blok"
        "hard_block" -> "Qattiq blok"
        else -> "Ruxsat"
    }
}
