package uz.qalqon.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserAccount::class,
        ParentProfile::class,
        ChildProfile::class,
        ProtectedApp::class,
        ActivityLog::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userAccountDao(): UserAccountDao
    abstract fun parentProfileDao(): ParentProfileDao
    abstract fun childProfileDao(): ChildProfileDao
    abstract fun protectedAppDao(): ProtectedAppDao
    abstract fun activityLogDao(): ActivityLogDao
}
