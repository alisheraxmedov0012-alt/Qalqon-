package uz.qalqon.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "protected_apps")
data class ProtectedApp(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packageName: String,
    val appDisplayName: String,
    val isProtected: Boolean = true
)

