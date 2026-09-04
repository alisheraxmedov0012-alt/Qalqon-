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
}

