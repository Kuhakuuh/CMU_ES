package com.example.escmu.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.escmu.database.models.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Upsert
    suspend fun upsertExpense(expense: Expense)
    @Delete
    suspend fun deleteExpense(expense: Expense)
    @Query("SELECT * FROM expenses ")
    fun getExpense(): Flow<List<Expense>>
    @Query("SELECT * FROM expenses WHERE id=:id ")
    fun getExpenseById(id: String): LiveData<Expense>
    @Query("DELETE FROM  expenses")
    suspend fun deleteAllExpenses()
}