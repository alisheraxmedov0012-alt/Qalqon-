package uz.qalqon.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_accounts")
data class UserAccount(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val phoneNumber: String,
    val pinHash: String,
    val createdAt: Long = System.currentTimeMillis()
)

