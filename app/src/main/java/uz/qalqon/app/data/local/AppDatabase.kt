package uz.qalqon.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UserAccount::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userAccountDao(): UserAccountDao
}

