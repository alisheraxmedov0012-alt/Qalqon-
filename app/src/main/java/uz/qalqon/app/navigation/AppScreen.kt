package uz.qalqon.app.navigation

sealed class AppScreen(val route: String) {
    data object Welcome : AppScreen("welcome")
    data object Register : AppScreen("register")
    data object Login : AppScreen("login")
    data object CreatePin : AppScreen("create_pin")
    data object Home : AppScreen("home")
}
