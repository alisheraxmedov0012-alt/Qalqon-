package uz.qalqon.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.qalqon.app.R
import uz.qalqon.app.data.recognition.RecognitionDebugRepository
import uz.qalqon.app.data.recognition.RecognitionResult

@Composable
fun RecognitionDebugScreen(
    recognitionDebugRepository: RecognitionDebugRepository,
    onBackClick: () -> Unit
) {
    val result by recognitionDebugRepository.result.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.recognition_debug_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = stringResource(R.string.recognition_current_result))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = recognitionText(result))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { recognitionDebugRepository.setParent() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.recognition_set_parent))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { recognitionDebugRepository.setChild() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.recognition_set_child))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { recognitionDebugRepository.setUnknown() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.recognition_set_unknown))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { recognitionDebugRepository.setNoFace() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.recognition_set_no_face))
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.btn_back))
        }
    }
}

@Composable
private fun recognitionText(result: RecognitionResult): String {
    return when (result) {
        is RecognitionResult.NoFace -> stringResource(R.string.recognition_no_face)
        is RecognitionResult.ParentRecognized -> {
            "Ota-ona tanildi (${(result.confidence * 100).toInt()}%)"
        }
        is RecognitionResult.ChildRecognized -> {
            "Bola tanildi: ${result.childName} (${(result.confidence * 100).toInt()}%)"
        }
        is RecognitionResult.Unknown -> {
            "Noma'lum foydalanuvchi (${(result.confidence * 100).toInt()}%)"
        }
    }
}
