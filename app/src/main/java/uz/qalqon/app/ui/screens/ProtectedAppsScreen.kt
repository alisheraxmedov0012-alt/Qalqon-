package uz.qalqon.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uz.qalqon.app.R
import uz.qalqon.app.data.local.ProtectedApp
import uz.qalqon.app.data.local.ProtectedAppDao
import androidx.compose.ui.res.stringResource

@Composable
fun ProtectedAppsScreen(
    protectedAppDao: ProtectedAppDao,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var apps by remember { mutableStateOf(listOf<ProtectedApp>()) }

    suspend fun refresh() {
        apps = protectedAppDao.getAll()
    }

    LaunchedEffect(Unit) {
        if (protectedAppDao.getAll().isEmpty()) {
            protectedAppDao.insert(
                ProtectedApp(
                    packageName = "com.youtube.app",
                    appDisplayName = "YouTube"
                )
            )
            protectedAppDao.insert(
                ProtectedApp(
                    packageName = "com.instagram.app",
                    appDisplayName = "Instagram"
                )
            )
            protectedAppDao.insert(
                ProtectedApp(
                    packageName = "org.telegram.messenger",
                    appDisplayName = "Telegram"
                )
            )
        }
        refresh()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.protected_apps_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (apps.isEmpty()) {
            Text(text = stringResource(R.string.protected_apps_empty))
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(apps) { app ->
                    ProtectedAppItem(
                        app = app,
                        onToggle = {
                            scope.launch {
                                protectedAppDao.update(
                                    app.copy(isProtected = !app.isProtected)
                                )
                                refresh()
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onBackClick) {
            Text(text = stringResource(R.string.btn_back))
        }
    }
}

@Composable
private fun ProtectedAppItem(
    app: ProtectedApp,
    onToggle: () -> Unit
) {
    androidx.compose.material3.Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = app.appDisplayName, style = MaterialTheme.typography.titleMedium)
            Text(text = app.packageName, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onToggle) {
                Text(
                    text = if (app.isProtected) {
                        "Himoyadan chiqarish"
                    } else {
                        "Himoyaga qo'shish"
                    }
                )
            }
        }
    }
}

