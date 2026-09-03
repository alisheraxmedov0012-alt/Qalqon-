package uz.qalqon.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ParentProfileDao {

    @Query("SELECT * FROM parent_profiles WHERE accountId = :accountId LIMIT 1")
    suspend fun getByAccountId(accountId: Int): ParentProfile?

    @Insert
    suspend fun insert(profile: ParentProfile): Long

    @Update
    suspend fun update(profile: ParentProfile)
}

