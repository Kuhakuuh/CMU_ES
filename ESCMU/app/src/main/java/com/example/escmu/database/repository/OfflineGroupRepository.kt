package com.example.escmu.database.repository

import com.example.escmu.database.dao.GroupDao
import com.example.escmu.database.models.Expense
import com.example.escmu.database.models.Group
import kotlinx.coroutines.flow.Flow

class OfflineGroupRepository(
    private val groupDao: GroupDao
):GroupRepository {
    override fun getGroupStream(): Flow<List<Group>> = groupDao.getGroup()
    override suspend fun insertGroup(group: Group) = groupDao.upsertGroup(group)

    override suspend fun deleteGroup(group: Group) = groupDao.deleteGroup(group)

    override suspend fun updateGroup(group: Group) = groupDao.upsertGroup(group)

    override suspend fun deleteAllGroups() = groupDao.deleteAllGroups()

}