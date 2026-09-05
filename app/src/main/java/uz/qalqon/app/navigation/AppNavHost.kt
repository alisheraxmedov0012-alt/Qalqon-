package uz.qalqon.app.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import uz.qalqon.app.data.local.ProtectedAppDao
import uz.qalqon.app.data.protection.ProtectionDebugEngine
import uz.qalqon.app.data.protection.ProtectionOverlayController
import uz.qalqon.app.data.recognition.RecognitionDebugRepository
import uz.qalqon.app.data.repository.ActivityLogRepository
import uz.qalqon.app.data.repository.AppResetRepository
import uz.qalqon.app.data.repository.AuthRepository
import uz.qalqon.app.data.repository.ProfileRepository
import uz.qalqon.app.data.session.SessionManager
import uz.qalqon.app.data.settings.SettingsRepository
import uz.qalqon.app.ui.screens.*

@Composable
fun AppNavHost(
    authRepository: AuthRepository,
    sessionManager: SessionManager,
    profileRepository: ProfileRepository,
    settingsRepository: SettingsRepository,
    protectedAppDao: ProtectedAppDao,
    activityLogRepository: ActivityLogRepository,
    appResetRepository: AppResetRepository
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val recognitionDebugRepository = remember { RecognitionDebugRepository() }
    val protectionDebugEngine = remember { ProtectionDebugEngine() }
    val protectionOverlayController = remember { ProtectionOverlayController() }

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
                            activityLogRepository.add("login", "Foydalanuvchi tizimga kirdi")
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
                            activityLogRepository.add("register", "Yangi foydalanuvchi ro'yxatdan o'tdi")
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
                sessionManager = sessionManager,
                authRepository = authRepository,
                profileRepository = profileRepository,
                settingsRepository = settingsRepository,
                onParentProfileClick = { navController.navigate(AppScreen.ParentProfile.route) },
                onChildProfilesClick = { navController.navigate(AppScreen.ChildProfiles.route) },
                onSettingsClick = { navController.navigate(AppScreen.Settings.route) },
                onProtectedAppsClick = { navController.navigate(AppScreen.ProtectedApps.route) },
                onRecognitionDebugClick = { navController.navigate(AppScreen.RecognitionDebug.route) },
                onProtectionDebugClick = { navController.navigate(AppScreen.ProtectionDebug.route) },
                onActivityLogClick = { navController.navigate(AppScreen.ActivityLog.route) },
                onPrivacyClick = { navController.navigate(AppScreen.Privacy.route) },
                onHelpClick = { navController.navigate(AppScreen.Help.route) }
            )
        }

        composable(AppScreen.ParentProfile.route) {
            ParentProfileScreen(
                sessionManager = sessionManager,
                profileRepository = profileRepository,
                onBackClick = { navController.popBackStack() },
                onEnrollFaceClick = { navController.navigate(AppScreen.ParentFaceEnrollment.route) }
            )
        }

        composable(AppScreen.ChildProfiles.route) {
            ChildProfilesScreen(
                sessionManager = sessionManager,
                profileRepository = profileRepository,
                onBackClick = { navController.popBackStack() },
                onEnrollFaceClick = { childId ->
                    navController.navigate(AppScreen.ChildFaceEnrollment.createRoute(childId))
                }
            )
        }

        composable(AppScreen.Settings.route) {
            SettingsScreen(
                settingsRepository = settingsRepository,
                appResetRepository = appResetRepository,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppScreen.ProtectedApps.route) {
            ProtectedAppsScreen(
                protectedAppDao = protectedAppDao,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppScreen.ParentFaceEnrollment.route) {
            ParentFaceEnrollmentScreen(
                sessionManager = sessionManager,
                profileRepository = profileRepository,
                onBackClick = { navController.popBackStack() },
                onEnrollmentComplete = {
                    scope.launch {
                        activityLogRepository.add("parent_face", "Ota-ona yuzi ro'yxatdan o'tkazildi")
                    }
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppScreen.ChildFaceEnrollment.route,
            arguments = listOf(navArgument("childId") { type = NavType.IntType })
        ) { backStackEntry ->
            val childId = backStackEntry.arguments?.getInt("childId") ?: 0
            ChildFaceEnrollmentScreen(
                childId = childId,
                sessionManager = sessionManager,
                profileRepository = profileRepository,
                onBackClick = { navController.popBackStack() },
                onEnrollmentComplete = {
                    scope.launch {
                        activityLogRepository.add("child_face", "Bola yuzi ro'yxatdan o'tkazildi")
                    }
                    navController.popBackStack()
                }
            )
        }

        composable(AppScreen.RecognitionDebug.route) {
            RecognitionDebugScreen(
                recognitionDebugRepository = recognitionDebugRepository,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppScreen.ProtectionDebug.route) {
            ProtectionDebugScreen(
                recognitionDebugRepository = recognitionDebugRepository,
                settingsRepository = settingsRepository,
                protectionDebugEngine = protectionDebugEngine,
                protectionOverlayController = protectionOverlayController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppScreen.ActivityLog.route) {
            ActivityLogScreen(
                activityLogRepository = activityLogRepository,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppScreen.Privacy.route) {
            PrivacyScreen(onBackClick = { navController.popBackStack() })
        }

        composable(AppScreen.Help.route) {
            HelpScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
