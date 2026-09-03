package uz.qalqon.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.qalqon.app.R

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.register_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.register_subtitle),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text(stringResource(R.string.label_full_name)) },
            modifier = Modifier.fillMaxSize().weight(0.0f, false)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(stringResource(R.string.label_phone)) },
            modifier = Modifier.fillMaxSize().weight(0.0f, false)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onContinueClick,
            enabled = fullName.isNotBlank() && phone.isNotBlank()
        ) {
            Text(text = stringResource(R.string.btn_continue))
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onBackClick) {
            Text(text = stringResource(R.string.btn_back))
        }
    }
}
