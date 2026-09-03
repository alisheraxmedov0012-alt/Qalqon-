package uz.qalqon.app.data.repository

import uz.qalqon.app.data.local.UserAccount
import uz.qalqon.app.data.local.UserAccountDao
import java.security.MessageDigest

class AuthRepository(
    private val userDao: UserAccountDao
) {
    suspend fun register(fullName: String, phone: String, pin: String): Result<Int> {
        val normalizedPhone = phone.trim()

        val existing = userDao.getByPhone(normalizedPhone)
        if (existing != null) {
            return Result.failure(Exception("duplicate_phone"))
        }

        val userId = userDao.insert(
            UserAccount(
                fullName = fullName.trim(),
                phoneNumber = normalizedPhone,
                pinHash = hashPin(pin)
            )
        ).toInt()

        return Result.success(userId)
    }

    suspend fun login(phone: String, pin: String): Result<Int> {
        val user = userDao.getByPhone(phone.trim())
            ?: return Result.failure(Exception("invalid_credentials"))

        return if (user.pinHash == hashPin(pin)) {
            Result.success(user.id)
        } else {
            Result.failure(Exception("invalid_credentials"))
        }
    }

    suspend fun getUserById(id: Int): UserAccount? {
        return userDao.getById(id)
    }

    private fun hashPin(pin: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(pin.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

