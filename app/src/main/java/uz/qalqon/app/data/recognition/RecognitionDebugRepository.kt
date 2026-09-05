package uz.qalqon.app.data.recognition

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecognitionDebugRepository {

    private val _result = MutableStateFlow<RecognitionResult>(RecognitionResult.NoFace)
    val result: StateFlow<RecognitionResult> = _result

    fun setNoFace() {
        _result.value = RecognitionResult.NoFace
    }

    fun setParent(confidence: Float = 0.96f) {
        _result.value = RecognitionResult.ParentRecognized(confidence)
    }

    fun setChild(childName: String = "Ali", confidence: Float = 0.91f) {
        _result.value = RecognitionResult.ChildRecognized(childName, confidence)
    }

    fun setUnknown(confidence: Float = 0.52f) {
        _result.value = RecognitionResult.Unknown(confidence)
    }
}
