package uz.qalqon.app.data.session

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionManager(private val context: Context) {

    companion object {
        private val LOGGED_IN_USER_ID = intPreferencesKey("logged_in_user_id")
    }

    val loggedInUserId: Flow<Int?> = context.dataStore.data.map { prefs ->
        prefs[LOGGED_IN_USER_ID]
    }

    suspend fun saveLoggedInUserId(userId: Int) {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN_USER_ID] = userId
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(LOGGED_IN_USER_ID)
        }
    }
}

