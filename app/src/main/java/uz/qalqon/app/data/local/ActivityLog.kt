package uz.qalqon.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_logs")
data class ActivityLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventType: String,
    val message: String,
    val createdAt: Long = System.currentTimeMillis()
)
