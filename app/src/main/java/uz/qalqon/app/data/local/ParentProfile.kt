package uz.qalqon.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parent_profiles")
data class ParentProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val accountId: Int,
    val displayName: String,
    val isFaceEnrolled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

