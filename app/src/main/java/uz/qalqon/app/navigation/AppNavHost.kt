package uz.qalqon.app.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import uz.qalqon.app.data.repository.AuthRepository
import uz.qalqon.app.data.session.SessionManager
import uz.qalqon.app.ui.screens.CreatePinScreen
import uz.qalqon.app.ui.screens.HomeScreen
import uz.qalqon.app.ui.screens.LoginScreen
import uz.qalqon.app.ui.screens.RegisterScreen
import uz.qalqon.app.ui.screens.WelcomeScreen

@Composable
fun AppNavHost(
    authRepository: AuthRepository,
    sessionManager: SessionManager
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    var pendingFullName by remember { mutableStateOf("") }
    var pendingPhone by remember { mutableStateOf("") }

    val loggedInUserId by sessionManager.loggedInUserId.collectAsState(initial = null)
    val startDestination = if (loggedInUserId != null) AppScreen.Home.route else AppScreen.Welcome.route

    NavHost(
        navController = navController,
        startDestination = startDestination
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
                onContinueClick = { fullName, phone ->
                    pendingFullName = fullName
                    pendingPhone = phone
                    navController.navigate(AppScreen.CreatePin.route)
                }
            )
        }

        composable(AppScreen.Login.route) {
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                onLoginSuccess = { phone, pin ->
                    scope.launch {
                        val result = authRepository.login(phone, pin)
                        result.onSuccess { userId ->
                            sessionManager.saveLoggedInUserId(userId)
                            navController.navigate(AppScreen.Home.route) {
                                popUpTo(0)
                            }
                        }
                    }
                }
            )
        }

        composable(AppScreen.CreatePin.route) {
            CreatePinScreen(
                onBackClick = { navController.popBackStack() },
                onPinCreated = { pin ->
                    scope.launch {
                        val result = authRepository.register(
                            fullName = pendingFullName,
                            phone = pendingPhone,
                            pin = pin
                        )
                        result.onSuccess { userId ->
                            sessionManager.saveLoggedInUserId(userId)
                            navController.navigate(AppScreen.Home.route) {
                                popUpTo(0)
                            }
                        }
                    }
                }
            )
        }

        composable(AppScreen.Home.route) {
            HomeScreen(
                sessionManager = sessionManager
            )
        }
    }
}
