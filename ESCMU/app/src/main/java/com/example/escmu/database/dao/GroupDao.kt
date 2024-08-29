package com.example.escmu.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.escmu.database.models.Expense
import com.example.escmu.database.models.Group
import kotlinx.coroutines.flow.Flow
@Dao
interface GroupDao {

    @Upsert
    suspend fun upsertGroup(group: Group)

    @Delete
    suspend fun deleteGroup(group: Group)

    @Query("SELECT * FROM groups ")
    fun getGroup(): Flow<List<Group>>

    @Query("DELETE FROM groups")
    suspend fun deleteAllGroups()
}
