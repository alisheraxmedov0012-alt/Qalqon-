package uz.qalqon.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uz.qalqon.app.R
import uz.qalqon.app.data.repository.AuthRepository
import uz.qalqon.app.data.repository.ProfileRepository
import uz.qalqon.app.data.session.SessionManager
import uz.qalqon.app.data.settings.SettingsRepository
import uz.qalqon.app.data.settings.AppSettings

@Composable
fun HomeScreen(
    sessionManager: SessionManager,
    authRepository: AuthRepository,
    profileRepository: ProfileRepository,
    settingsRepository: SettingsRepository,
    onParentProfileClick: () -> Unit,
    onChildProfilesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProtectedAppsClick: () -> Unit,
    onRecognitionDebugClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val loggedInUserId by sessionManager.loggedInUserId.collectAsState(initial = null)
    val settings by settingsRepository.settingsFlow.collectAsState(initial = AppSettings())

    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var parentStatus by remember { mutableStateOf("") }
    var childCount by remember { mutableStateOf(0) }

    LaunchedEffect(loggedInUserId) {
        val userId = loggedInUserId ?: return@LaunchedEffect
        val user = authRepository.getUserById(userId)
        fullName = user?.fullName ?: ""
        phone = user?.phoneNumber ?: ""

        val parent = profileRepository.getParentProfile(userId)
        parentStatus = if (parent == null) {
            "Yaratilmagan"
        } else {
            parent.displayName
        }

        childCount = profileRepository.getChildProfiles(userId).size
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.home_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (settings.protectionEnabled) {
                stringResource(R.string.home_status_on)
            } else {
                stringResource(R.string.home_status_off)
            },
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (fullName.isNotBlank()) {
            Text(text = "${stringResource(R.string.home_user_name)}: $fullName")
        }

        if (phone.isNotBlank()) {
            Text(text = "${stringResource(R.string.home_user_phone)}: $phone")
        }

        Text(text = "${stringResource(R.string.home_parent_status)}: $parentStatus")
        Text(text = "${stringResource(R.string.home_children_count)}: $childCount")
        Text(text = "${stringResource(R.string.home_scan_mode)}: ${scanModeLabel(settings.scanMode)}")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onParentProfileClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.home_menu_parent))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onChildProfilesClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.home_menu_children))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onSettingsClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.home_menu_settings))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onProtectedAppsClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.home_menu_protected_apps))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onRecognitionDebugClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.home_menu_recognition_debug))
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                scope.launch {
                    sessionManager.clearSession()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.btn_logout))
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
