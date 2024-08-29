package com.example.escmu.database.repository

import com.example.escmu.database.models.Expense
import com.example.escmu.database.models.Group
import kotlinx.coroutines.flow.Flow

interface GroupRepository {

    /**
     * Retrieve all the groups from the the given data source.
     */
    fun getGroupStream(): Flow<List<Group>>


    /**
     * Insert a group in the data source
     */
    suspend fun insertGroup(group: Group)

    /**
     * Delete a group from the data source
     */
    suspend fun deleteGroup(group: Group)

    /**
     * Update group in the data source
     */
    suspend fun updateGroup(group: Group)

    suspend fun deleteAllGroups()

}