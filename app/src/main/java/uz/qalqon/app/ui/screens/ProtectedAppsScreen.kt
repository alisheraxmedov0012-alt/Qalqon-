package uz.qalqon.app.ui.screens

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uz.qalqon.app.R
import uz.qalqon.app.data.local.ProtectedApp
import uz.qalqon.app.data.local.ProtectedAppDao

data class InstalledAppItem(
    val packageName: String,
    val appDisplayName: String,
    val isProtected: Boolean
)

@Composable
fun ProtectedAppsScreen(
    protectedAppDao: ProtectedAppDao,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val scope = rememberCoroutineScope()

    val apps = remember { mutableStateListOf<InstalledAppItem>() }
    var loading by remember { mutableStateOf(true) }

    suspend fun loadApps() {
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { appInfo ->
                packageManager.getLaunchIntentForPackage(appInfo.packageName) != null &&
                    (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0
            }
            .map { appInfo ->
                val label = packageManager.getApplicationLabel(appInfo).toString()
                val existing = protectedAppDao.getByPackageName(appInfo.packageName)
                InstalledAppItem(
                    packageName = appInfo.packageName,
                    appDisplayName = label,
                    isProtected = existing?.isProtected ?: false
                )
            }
            .sortedBy { it.appDisplayName.lowercase() }

        apps.clear()
        apps.addAll(installedApps)
        loading = false
    }

    LaunchedEffect(Unit) {
        loadApps()
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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.protected_apps_subtitle),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            Text(text = stringResource(R.string.loading_text))
        } else if (apps.isEmpty()) {
            Text(text = stringResource(R.string.protected_apps_empty))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(apps, key = { it.packageName }) { app ->
                    ProtectedAppItem(
                        app = app,
                        onToggle = { checked ->
                            scope.launch {
                                val existing = protectedAppDao.getByPackageName(app.packageName)
                                if (existing == null) {
                                    protectedAppDao.insert(
                                        ProtectedApp(
                                            packageName = app.packageName,
                                            appDisplayName = app.appDisplayName,
                                            isProtected = checked
                                        )
                                    )
                                } else {
                                    protectedAppDao.update(
                                        existing.copy(
                                            appDisplayName = app.appDisplayName,
                                            isProtected = checked
                                        )
                                    )
                                }

                                val index = apps.indexOfFirst { it.packageName == app.packageName }
                                if (index != -1) {
                                    apps[index] = apps[index].copy(isProtected = checked)
                                }
                            }
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
private fun ProtectedAppItem(
    app: InstalledAppItem,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = app.appDisplayName,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = app.packageName,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (app.isProtected) {
                        stringResource(R.string.protected_apps_on)
                    } else {
                        stringResource(R.string.protected_apps_off)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Switch(
                    checked = app.isProtected,
                    onCheckedChange = onToggle
                )
            }
        }
    }
}
