package uz.qalqon.app.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import uz.qalqon.app.R
import uz.qalqon.app.data.repository.ProfileRepository
import uz.qalqon.app.data.session.SessionManager

@Composable
fun ParentFaceEnrollmentScreen(
    sessionManager: SessionManager,
    profileRepository: ProfileRepository,
    onBackClick: () -> Unit,
    onEnrollmentComplete: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val loggedInUserId by sessionManager.loggedInUserId.collectAsState(initial = null)

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var currentStep by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.parent_face_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!hasCameraPermission) {
            Text(text = stringResource(R.string.camera_permission_needed))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = stringResource(R.string.camera_permission_note))
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { hasCameraPermission = true }) {
                Text(text = stringResource(R.string.btn_permission_given_placeholder))
            }
        } else {
            Text(text = stringResource(R.string.enrollment_intro))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = currentEnrollmentStepText(currentStep))

            Spacer(modifier = Modifier.height(24.dp))

            if (currentStep < 4) {
                Button(onClick = { currentStep++ }) {
                    Text(text = stringResource(R.string.btn_next_step))
                }
            } else {
                Button(
                    onClick = {
                        val userId = loggedInUserId ?: return@Button
                        scope.launch {
                            profileRepository.markParentFaceEnrolled(userId, true)
                            onEnrollmentComplete()
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.btn_finish_enrollment))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(onClick = onBackClick) {
            Text(text = stringResource(R.string.btn_back))
        }
    }
}

@Composable
internal fun currentEnrollmentStepText(step: Int): String {
    return when (step) {
        1 -> stringResource(R.string.enroll_step_1)
        2 -> stringResource(R.string.enroll_step_2)
        3 -> stringResource(R.string.enroll_step_3)
        else -> stringResource(R.string.enroll_step_4)
    }
}
