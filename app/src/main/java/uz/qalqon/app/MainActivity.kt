package uz.qalqon.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.room.Room
import uz.qalqon.app.data.local.AppDatabase
import uz.qalqon.app.data.repository.AuthRepository
import uz.qalqon.app.data.repository.ProfileRepository
import uz.qalqon.app.data.session.SessionManager
import uz.qalqon.app.data.settings.SettingsRepository
import uz.qalqon.app.navigation.AppNavHost

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "qalqon_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    private val authRepository by lazy {
        AuthRepository(database.userAccountDao())
    }

    private val profileRepository by lazy {
        ProfileRepository(
            parentDao = database.parentProfileDao(),
            childDao = database.childProfileDao()
        )
    }

    private val sessionManager by lazy {
        SessionManager(applicationContext)
    }

    private val settingsRepository by lazy {
        SettingsRepository(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                AppNavHost(
                    authRepository = authRepository,
                    sessionManager = sessionManager,
                    profileRepository = profileRepository,
                    settingsRepository = settingsRepository,
                    protectedAppDao = database.protectedAppDao()
                )
            }
        }
    }
}
