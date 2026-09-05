package uz.qalqon.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.qalqon.app.R

@Composable
fun PrivacyScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.privacy_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.privacy_line_1))
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = stringResource(R.string.privacy_line_2))
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = stringResource(R.string.privacy_line_3))
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = stringResource(R.string.privacy_line_4))

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = onBackClick) {
            Text(text = stringResource(R.string.btn_back))
        }
    }
}

