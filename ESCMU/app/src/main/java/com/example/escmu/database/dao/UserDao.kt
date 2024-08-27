package com.example.escmu.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.escmu.database.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Upsert
    suspend fun upsertUser(userModel: User)

    @Delete
    suspend fun deleteUser(userModel: User)


    @Query("SELECT * FROM users ")
    fun getUser(): Flow<List<User>>

    @Query("DELETE FROM  users")
    suspend fun deleteAllUsers()
}