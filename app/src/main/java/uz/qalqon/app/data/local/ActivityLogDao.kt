package uz.qalqon.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ActivityLogDao {

    @Query("SELECT * FROM activity_logs ORDER BY createdAt DESC")
    suspend fun getAll(): List<ActivityLog>

    @Insert
    suspend fun insert(log: ActivityLog)

    @Query("DELETE FROM activity_logs")
    suspend fun clearAll()
}

