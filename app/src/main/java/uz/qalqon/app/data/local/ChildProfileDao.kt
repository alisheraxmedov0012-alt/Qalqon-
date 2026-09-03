package uz.qalqon.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ChildProfileDao {

    @Query("SELECT * FROM child_profiles WHERE accountId = :accountId ORDER BY createdAt DESC")
    suspend fun getAllByAccountId(accountId: Int): List<ChildProfile>

    @Insert
    suspend fun insert(profile: ChildProfile): Long

    @Update
    suspend fun update(profile: ChildProfile)

    @Delete
    suspend fun delete(profile: ChildProfile)
}

