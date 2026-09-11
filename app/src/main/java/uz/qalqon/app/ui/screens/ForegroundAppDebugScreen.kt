package uz.qalqon.app.ui.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.qalqon.app.R
import uz.qalqon.app.data.monitoring.ForegroundAppMonitor

@Composable
fun ForegroundAppDebugScreen(
    foregroundAppMonitor: ForegroundAppMonitor,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    var hasUsageAccess by remember { mutableStateOf(false) }
    var currentPackageName by remember { mutableStateOf("") }

    fun refreshState() {
        hasUsageAccess = foregroundAppMonitor.hasUsageAccess()
        currentPackageName = foregroundAppMonitor.getCurrentForegroundApp().orEmpty()
    }

    LaunchedEffect(Unit) {
        refreshState()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.foreground_debug_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (hasUsageAccess) {
                        stringResource(R.string.foreground_access_granted)
                    } else {
                        stringResource(R.string.foreground_access_missing)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (currentPackageName.isBlank()) {
                    Text(text = stringResource(R.string.foreground_current_empty))
                } else {
                    Text(text = "${stringResource(R.string.foreground_current_label)}: $currentPackageName")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = stringResource(R.string.foreground_debug_help))

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.foreground_open_settings))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { refreshState() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.foreground_refresh))
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.btn_back))
        }
    }
}
