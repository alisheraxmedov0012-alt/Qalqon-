package uz.qalqon.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.qalqon.app.R
import uz.qalqon.app.data.protection.ProtectionDebugEngine
import uz.qalqon.app.data.protection.ProtectionOverlayController
import uz.qalqon.app.data.protection.ProtectionState
import uz.qalqon.app.data.recognition.RecognitionDebugRepository
import uz.qalqon.app.data.settings.AppSettings
import uz.qalqon.app.data.settings.SettingsRepository

@Composable
fun ProtectionDebugScreen(
    recognitionDebugRepository: RecognitionDebugRepository,
    settingsRepository: SettingsRepository,
    protectionDebugEngine: ProtectionDebugEngine,
    protectionOverlayController: ProtectionOverlayController,
    onBackClick: () -> Unit
) {
    val recognitionResult by recognitionDebugRepository.result.collectAsState()
    val settings by settingsRepository.settingsFlow.collectAsState(initial = AppSettings())
    val protectionState by protectionDebugEngine.state.collectAsState()
    val isBlocked by protectionOverlayController.isBlocked.collectAsState()
    val blockMessage by protectionOverlayController.blockMessage.collectAsState()

    LaunchedEffect(recognitionResult, settings) {
        protectionDebugEngine.evaluate(recognitionResult, settings)
    }

    LaunchedEffect(protectionState) {
        when (protectionState) {
            ProtectionState.Unprotected -> protectionOverlayController.hideBlock()
            ProtectionState.Recovering -> protectionOverlayController.hideBlock()
            ProtectionState.SoftBlocked -> protectionOverlayController.showBlock("Yumshoq himoya bloklandi")
            ProtectionState.HardBlocked -> protectionOverlayController.showBlock("Qattiq himoya bloklandi")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.protection_debug_title),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = stringResource(R.string.protection_current_state))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = protectionText(protectionState))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = stringResource(R.string.protection_debug_hint))

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { recognitionDebugRepository.setParent() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.recognition_set_parent))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { recognitionDebugRepository.setChild() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.recognition_set_child))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { recognitionDebugRepository.setUnknown() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.recognition_set_unknown))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { recognitionDebugRepository.setNoFace() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.recognition_set_no_face))
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = { protectionDebugEngine.setRecovering() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.protection_set_recovering))
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = { protectionDebugEngine.reset() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.protection_reset))
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.btn_back))
            }
        }

        if (isBlocked) {
            ProtectionOverlayScreen(
                expectedPin = "1234",
                onUnlockSuccess = {
                    protectionOverlayController.hideBlock()
                    protectionDebugEngine.reset()
                },
                message = blockMessage
            )
        }
    }
}

@Composable
private fun protectionText(state: ProtectionState): String {
    return when (state) {
        ProtectionState.Unprotected -> stringResource(R.string.protection_state_unprotected)
        ProtectionState.SoftBlocked -> stringResource(R.string.protection_state_soft)
        ProtectionState.HardBlocked -> stringResource(R.string.protection_state_hard)
        ProtectionState.Recovering -> stringResource(R.string.protection_state_recovering)
    }
}
