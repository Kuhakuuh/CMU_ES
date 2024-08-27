package com.example.escmu.database.repository

import com.example.escmu.database.models.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {


    /**
     * Retrieve all the expenses from the the given data source.
     */
    fun getExpenseStream(): Flow<List<Expense>>


    /**
     * Insert an expense in the data source
     */
    suspend fun insertExpense(expense: Expense)

    /**
     * Delete an expense from the data source
     */
    suspend fun deleteExpense(expense: Expense)

    /**
     * Update expense in the data source
     */
    suspend fun updateExpense(expense: Expense)

    suspend fun deleteAllExpenses()
}