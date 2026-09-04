package uz.qalqon.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProtectedAppDao {

    @Query("SELECT * FROM protected_apps ORDER BY appDisplayName ASC")
    suspend fun getAll(): List<ProtectedApp>

    @Insert
    suspend fun insert(app: ProtectedApp): Long

    @Update
    suspend fun update(app: ProtectedApp)

    @Query("SELECT * FROM protected_apps WHERE packageName = :packageName LIMIT 1")
    suspend fun getByPackageName(packageName: String): ProtectedApp?
}
