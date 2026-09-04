package uz.qalqon.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uz.qalqon.app.R
import uz.qalqon.app.data.repository.ProfileRepository
import uz.qalqon.app.data.session.SessionManager

@Composable
fun ParentProfileScreen(
    sessionManager: SessionManager,
    profileRepository: ProfileRepository,
    onBackClick: () -> Unit,
    onEnrollFaceClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val loggedInUserId by sessionManager.loggedInUserId.collectAsState(initial = null)

    var displayName by remember { mutableStateOf("") }
    var savedMessage by remember { mutableStateOf("") }
    var isFaceEnrolled by remember { mutableStateOf(false) }

    LaunchedEffect(loggedInUserId) {
        val userId = loggedInUserId ?: return@LaunchedEffect
        val profile = profileRepository.getParentProfile(userId)
        displayName = profile?.displayName ?: ""
        isFaceEnrolled = profile?.isFaceEnrolled ?: false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.parent_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text(stringResource(R.string.parent_display_name_hint)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (isFaceEnrolled) {
                stringResource(R.string.face_enrolled)
            } else {
                stringResource(R.string.face_not_enrolled)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val userId = loggedInUserId ?: return@Button
                scope.launch {
                    profileRepository.saveParentProfile(userId, displayName)
                    savedMessage = stringResource(R.string.saved_text)
                }
            },
            enabled = displayName.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.btn_save))
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onEnrollFaceClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.btn_enroll_face))
        }

        if (savedMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = savedMessage)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.btn_back))
        }
    }
}
