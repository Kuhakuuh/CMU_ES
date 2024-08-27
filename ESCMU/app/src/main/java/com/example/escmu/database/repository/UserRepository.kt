package com.example.escmu.database.repository


import com.example.escmu.database.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {


     /**
      * Retrieve all the items from the the given data source.
      */
     fun getUserStream(): Flow<List<User>>


     /**
      * Insert item in the data source
      */
     suspend fun insertUser(user: User)

     /**
      * Delete item from the data source
      */
     suspend fun deleteUser(user: User)

     /**
      * Update item in the data source
      */
     suspend fun updateUser(user: User)

     suspend fun deleteAll()
}