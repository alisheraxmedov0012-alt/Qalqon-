package uz.qalqon.app.data.recognition

sealed class RecognitionResult {
    data object NoFace : RecognitionResult()
    data class ParentRecognized(val confidence: Float) : RecognitionResult()
    data class ChildRecognized(val childName: String, val confidence: Float) : RecognitionResult()
    data class Unknown(val confidence: Float) : RecognitionResult()
}
