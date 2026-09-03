package uz.qalqon.app.ui.screens

import androidx.compose.foundation.layout.*
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
fun CreatePinScreen(
    onBackClick: () -> Unit,
    onPinCreated: (String) -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }

    val isValid = pin.length in listOf(4, 6) && pin == confirmPin

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.createpin_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.createpin_subtitle),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = pin,
            onValueChange = { pin = it },
            label = { Text(stringResource(R.string.label_pin)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPin,
            onValueChange = { confirmPin = it },
            label = { Text(stringResource(R.string.label_pin_confirm)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onPinCreated(pin) },
            enabled = isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.btn_create))
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.btn_back))
        }
    }
}
