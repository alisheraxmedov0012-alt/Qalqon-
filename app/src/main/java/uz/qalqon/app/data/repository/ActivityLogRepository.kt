package uz.qalqon.app.data.repository

import uz.qalqon.app.data.local.ActivityLog
import uz.qalqon.app.data.local.ActivityLogDao

class ActivityLogRepository(
    private val dao: ActivityLogDao
) {
    suspend fun add(eventType: String, message: String) {
        dao.insert(
            ActivityLog(
                eventType = eventType,
                message = message
            )
        )
    }

    suspend fun getAll(): List<ActivityLog> = dao.getAll()

    suspend fun clearAll() = dao.clearAll()
}

