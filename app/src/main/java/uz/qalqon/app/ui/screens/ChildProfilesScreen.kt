package uz.qalqon.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uz.qalqon.app.R
import uz.qalqon.app.data.local.ChildProfile
import uz.qalqon.app.data.repository.ProfileRepository
import uz.qalqon.app.data.session.SessionManager

@Composable
fun ChildProfilesScreen(
    sessionManager: SessionManager,
    profileRepository: ProfileRepository,
    onBackClick: () -> Unit,
    onEnrollFaceClick: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val loggedInUserId by sessionManager.loggedInUserId.collectAsState(initial = null)

    var childName by remember { mutableStateOf("") }
    var children by remember { mutableStateOf(listOf<ChildProfile>()) }

    suspend fun refresh() {
        val userId = loggedInUserId ?: return
        children = profileRepository.getChildProfiles(userId)
    }

    LaunchedEffect(loggedInUserId) {
        refresh()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.children_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = childName,
            onValueChange = { childName = it },
            label = { Text(stringResource(R.string.children_name_hint)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val userId = loggedInUserId ?: return@Button
                scope.launch {
                    profileRepository.addChildProfile(
                        accountId = userId,
                        childName = childName,
                        restrictionLevel = "medium"
                    )
                    childName = ""
                    refresh()
                }
            },
            enabled = childName.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.children_add))
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (children.isEmpty()) {
            Text(text = stringResource(R.string.children_empty))
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(children) { child ->
                    ChildItem(
                        child = child,
                        onDelete = {
                            scope.launch {
                                profileRepository.deleteChildProfile(child)
                                refresh()
                            }
                        },
                        onEnrollFace = {
                            onEnrollFaceClick(child.id)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.btn_back))
        }
    }
}

@Composable
private fun ChildItem(
    child: ChildProfile,
    onDelete: () -> Unit,
    onEnrollFace: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = child.childName, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${stringResource(R.string.children_level_label)}: ${child.restrictionLevel}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (child.isFaceEnrolled) {
                    stringResource(R.string.face_enrolled)
                } else {
                    stringResource(R.string.face_not_enrolled)
                },
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onEnrollFace,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.btn_enroll_face))
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.btn_delete))
            }
        }
    }
}
