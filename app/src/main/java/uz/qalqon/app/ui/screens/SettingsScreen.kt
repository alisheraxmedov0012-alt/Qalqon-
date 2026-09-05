package uz.qalqon.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uz.qalqon.app.R
import uz.qalqon.app.data.repository.AppResetRepository
import uz.qalqon.app.data.settings.SettingsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsRepository: SettingsRepository,
    appResetRepository: AppResetRepository,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val protectionEnabled by settingsRepository.isProtectionEnabled.collectAsState(initial = false)
    val lowBatterySaving by settingsRepository.isLowBatterySaving.collectAsState(initial = false)

    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.btn_back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.settings_protection),
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = protectionEnabled,
                    onCheckedChange = { enabled ->
                        scope.launch {
                            settingsRepository.setProtectionEnabled(enabled)
                        }
                    }
                )
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.settings_low_battery),
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = lowBatterySaving,
                    onCheckedChange = { enabled ->
                        scope.launch {
                            settingsRepository.setLowBatterySaving(enabled)
                        }
                    }
                )
            }

            HorizontalDivider()

            OutlinedButton(
                onClick = {
                    scope.launch {
                        appResetRepository.clearActivityLogs()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.settings_clear_logs))
            }

            Button(
                onClick = { showResetDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(text = stringResource(R.string.settings_reset_all))
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.settings_reset_all)) },
            text = { Text("Barcha ma'lumotlarni o'chirib tashlashni tasdiqlaysizmi?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetDialog = false
                        scope.launch {
                            appResetRepository.resetAll()
                            onBackClick()
                        }
                    }
                ) {
                    Text(stringResource(R.string.btn_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(stringResource(R.string.btn_back))
                }
            }
        )
    }
}
