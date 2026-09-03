package uz.qalqon.app.data.repository

import uz.qalqon.app.data.local.ChildProfile
import uz.qalqon.app.data.local.ChildProfileDao
import uz.qalqon.app.data.local.ParentProfile
import uz.qalqon.app.data.local.ParentProfileDao

class ProfileRepository(
    private val parentDao: ParentProfileDao,
    private val childDao: ChildProfileDao
) {

    suspend fun getParentProfile(accountId: Int): ParentProfile? {
        return parentDao.getByAccountId(accountId)
    }

    suspend fun saveParentProfile(accountId: Int, displayName: String) {
        val existing = parentDao.getByAccountId(accountId)
        if (existing == null) {
            parentDao.insert(
                ParentProfile(
                    accountId = accountId,
                    displayName = displayName.trim()
                )
            )
        } else {
            parentDao.update(
                existing.copy(
                    displayName = displayName.trim(),
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun getChildProfiles(accountId: Int): List<ChildProfile> {
        return childDao.getAllByAccountId(accountId)
    }

    suspend fun addChildProfile(accountId: Int, childName: String, restrictionLevel: String) {
        childDao.insert(
            ChildProfile(
                accountId = accountId,
                childName = childName.trim(),
                restrictionLevel = restrictionLevel
            )
        )
    }

    suspend fun deleteChildProfile(profile: ChildProfile) {
        childDao.delete(profile)
    }
}

