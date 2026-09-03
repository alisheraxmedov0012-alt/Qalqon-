package uz.qalqon.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserAccountDao {

    @Insert
    suspend fun insert(user: UserAccount): Long

    @Query("SELECT * FROM user_accounts WHERE phoneNumber = :phone LIMIT 1")
    suspend fun getByPhone(phone: String): UserAccount?

    @Query("SELECT * FROM user_accounts WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): UserAccount?
}

