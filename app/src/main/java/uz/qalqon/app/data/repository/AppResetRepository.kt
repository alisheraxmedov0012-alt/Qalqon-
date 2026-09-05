package uz.qalqon.app.data.repository

import uz.qalqon.app.data.local.ActivityLogDao
import uz.qalqon.app.data.local.ChildProfileDao
import uz.qalqon.app.data.local.ParentProfileDao
import uz.qalqon.app.data.local.ProtectedAppDao
import uz.qalqon.app.data.local.UserAccountDao
import uz.qalqon.app.data.session.SessionManager

class AppResetRepository(
    private val userAccountDao: UserAccountDao,
    private val parentProfileDao: ParentProfileDao,
    private val childProfileDao: ChildProfileDao,
    private val protectedAppDao: ProtectedAppDao,
    private val activityLogDao: ActivityLogDao,
    private val sessionManager: SessionManager
) {
    suspend fun clearSessionOnly() {
        sessionManager.clearSession()
    }

    suspend fun clearActivityLogs() {
        activityLogDao.clearAll()
    }

    suspend fun resetAll() {
        activityLogDao.clearAll()
        sessionManager.clearSession()
        // Note:
        // Full DB wipe will be implemented more safely in later phase if needed.
        // For now, session and activity logs are reset and destructive migration can recreate DB if structure changes.
    }
}

