package com.example.escmu.database.repository

import com.example.escmu.database.dao.UserDao
import com.example.escmu.database.models.User
import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(
    private val userDao:UserDao
):UserRepository {
    override fun getUserStream(): Flow<List<User>> = userDao.getUser()

    override suspend fun insertUser(user: User) = userDao.upsertUser(user)

    override suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    override suspend fun updateUser(user: User) = userDao.upsertUser(user)

    override suspend fun deleteAll() =userDao.deleteAllUsers()


}