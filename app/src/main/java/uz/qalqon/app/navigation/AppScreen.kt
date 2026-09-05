package uz.qalqon.app.navigation

sealed class AppScreen(val route: String) {
    data object Welcome : AppScreen("welcome")
    data object Register : AppScreen("register")
    data object Login : AppScreen("login")
    data object CreatePin : AppScreen("create_pin")
    data object Home : AppScreen("home")
    data object ParentProfile : AppScreen("parent_profile")
    data object ChildProfiles : AppScreen("child_profiles")
    data object Settings : AppScreen("settings")
    data object ProtectedApps : AppScreen("protected_apps")
    data object ParentFaceEnrollment : AppScreen("parent_face_enrollment")
    data object RecognitionDebug : AppScreen("recognition_debug")
    data object ProtectionDebug : AppScreen("protection_debug")
    data object ChildFaceEnrollment : AppScreen("child_face_enrollment/{childId}") {
        fun createRoute(childId: Int): String = "child_face_enrollment/$childId"
    }
}
