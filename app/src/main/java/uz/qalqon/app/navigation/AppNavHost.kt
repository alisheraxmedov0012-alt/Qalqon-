package uz.qalqon.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uz.qalqon.app.ui.screens.CreatePinScreen
import uz.qalqon.app.ui.screens.HomeScreen
import uz.qalqon.app.ui.screens.LoginScreen
import uz.qalqon.app.ui.screens.RegisterScreen
import uz.qalqon.app.ui.screens.WelcomeScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreen.Welcome.route
    ) {
        composable(AppScreen.Welcome.route) {
            WelcomeScreen(
                onRegisterClick = { navController.navigate(AppScreen.Register.route) },
                onLoginClick = { navController.navigate(AppScreen.Login.route) }
            )
        }

        composable(AppScreen.Register.route) {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onContinueClick = { navController.navigate(AppScreen.CreatePin.route) }
            )
        }

        composable(AppScreen.Login.route) {
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                onLoginSuccess = {
                    navController.navigate(AppScreen.Home.route) {
                        popUpTo(AppScreen.Welcome.route) { inclusive = false }
                    }
                }
            )
        }

        composable(AppScreen.CreatePin.route) {
            CreatePinScreen(
                onBackClick = { navController.popBackStack() },
                onPinCreated = {
                    navController.navigate(AppScreen.Home.route) {
                        popUpTo(AppScreen.Welcome.route) { inclusive = false }
                    }
                }
            )
        }

        composable(AppScreen.Home.route) {
            HomeScreen()
        }
    }
}
