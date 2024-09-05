package com.example.escmu.database.repository

import com.example.escmu.database.dao.ExpenseDao
import com.example.escmu.database.dao.UserDao
import com.example.escmu.database.models.Expense
import com.example.escmu.database.models.User
import kotlinx.coroutines.flow.Flow

class OfflineExpenseRepository(
    private val expenseDao: ExpenseDao
):ExpenseRepository {
    override fun getExpenseStream(): Flow<List<Expense>> = expenseDao.getExpense()

    override suspend fun insertExpense(expense: Expense) = expenseDao.upsertExpense(expense)
    override suspend fun getExpenseById(id: String) = expenseDao.getExpenseById(id)

    override suspend fun deleteExpense(expense: Expense) = expenseDao.deleteExpense(expense)

    override suspend fun updateExpense(expense: Expense) = expenseDao.upsertExpense(expense)

    override suspend fun deleteAllExpenses() = expenseDao.deleteAllExpenses()
}
