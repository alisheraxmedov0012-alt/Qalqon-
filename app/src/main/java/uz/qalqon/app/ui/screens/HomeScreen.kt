package uz.qalqon.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uz.qalqon.app.R
import uz.qalqon.app.data.repository.AuthRepository
import uz.qalqon.app.data.repository.ProfileRepository
import uz.qalqon.app.data.session.SessionManager
import uz.qalqon.app.data.settings.AppSettings
import uz.qalqon.app.data.settings.SettingsRepository

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
    onRecognitionDebugClick: () -> Unit,
    onProtectionDebugClick: () -> Unit,
    onActivityLogClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onHelpClick: () -> Unit,
    onForegroundDebugClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val loggedInUserId by sessionManager.loggedInUserId.collectAsState(initial = null)
    val settings by settingsRepository.settingsFlow.collectAsState(initial = AppSettings())

    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var parentStatus by remember { mutableStateOf("") }
    var childCount by remember { mutableIntStateOf(0) }
    var parentFaceReady by remember { mutableStateOf(false) }
    var anyChildFaceReady by remember { mutableStateOf(false) }
    var parentProfileReady by remember { mutableStateOf(false) }
    var protectedAppsSelected by remember { mutableStateOf(false) }

    LaunchedEffect(loggedInUserId) {
        val userId = loggedInUserId ?: return@LaunchedEffect
        val user = authRepository.getUserById(userId)
        fullName = user?.fullName ?: ""
        phone = user?.phoneNumber ?: ""

        val parent = profileRepository.getParentProfile(userId)
        parentProfileReady = parent != null
        parentFaceReady = parent?.isFaceEnrolled == true
        parentStatus = if (parent == null) {
            "Yaratilmagan"
        } else {
            parent.displayName
        }

        val children = profileRepository.getChildProfiles(userId)
        childCount = children.size
        anyChildFaceReady = children.any { it.isFaceEnrolled }

        protectedAppsSelected = false
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

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.setup_status_title),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                SetupRow(
                    title = stringResource(R.string.setup_account_ready),
                    ready = fullName.isNotBlank() && phone.isNotBlank()
                )

                SetupRow(
                    title = stringResource(R.string.setup_parent_profile_ready),
                    ready = parentProfileReady
                )

                SetupRow(
                    title = stringResource(R.string.setup_parent_face_ready),
                    ready = parentFaceReady
                )

                SetupRow(
                    title = stringResource(R.string.setup_child_added),
                    ready = childCount > 0
                )

                SetupRow(
                    title = stringResource(R.string.setup_child_face_ready),
                    ready = anyChildFaceReady
                )

                SetupRow(
                    title = stringResource(R.string.setup_protected_apps_ready),
                    ready = protectedAppsSelected
                )

                SetupRow(
                    title = stringResource(R.string.setup_protection_enabled),
                    ready = settings.protectionEnabled
                )
            }
        }

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

        Button(onClick = onProtectionDebugClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.home_menu_protection_debug))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onForegroundDebugClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.home_menu_foreground_debug))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onActivityLogClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.home_menu_activity_log))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onPrivacyClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.home_menu_privacy))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onHelpClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.home_menu_help))
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

@Composable
private fun SetupRow(
    title: String,
    ready: Boolean
) {
    Text(
        text = if (ready) {
            "Tayyor: $title"
        } else {
            "Kutilmoqda: $title"
        },
        style = MaterialTheme.typography.bodyMedium
    )
}

private fun scanModeLabel(mode: String): String {
    return when (mode) {
        "battery_saver" -> "Batareyani tejash"
        "strict" -> "Qattiq"
        else -> "Muvozanatli"
    }
}
