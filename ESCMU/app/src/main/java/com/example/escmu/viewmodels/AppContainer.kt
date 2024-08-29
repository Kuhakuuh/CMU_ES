package com.example.escmu.viewmodels

import android.content.Context
import com.example.escmu.database.MyDatabase
import com.example.escmu.database.repository.ExpenseRepository
import com.example.escmu.database.repository.GroupRepository
import com.example.escmu.database.repository.OfflineExpenseRepository
import com.example.escmu.database.repository.OfflineGroupRepository
import com.example.escmu.database.repository.OfflineUserRepository
import com.example.escmu.database.repository.UserRepository

/**
 * App container for dependency injection
 */
interface AppContainer{
    
    val userRepository :UserRepository
    val expenseRepository:ExpenseRepository
    val groupRepository:GroupRepository
}


class AppDataContainer(private val context: Context): AppContainer
{
    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(MyDatabase.getInstance(context).userDao)
    }

    override val expenseRepository: ExpenseRepository by lazy {
        OfflineExpenseRepository(MyDatabase.getInstance(context).expenseDao)
    }

    override val groupRepository: GroupRepository by lazy {
        OfflineGroupRepository(MyDatabase.getInstance(context).groupDao)
    }

}

