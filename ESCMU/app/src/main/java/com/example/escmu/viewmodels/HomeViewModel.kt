package com.example.escmu.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escmu.database.models.Expense
import com.example.escmu.database.models.User
import com.example.escmu.database.repository.ExpenseRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val expenseRepository:ExpenseRepository
):ViewModel() {
    private val _expenseData = MutableLiveData<Expense>()
    val expenseData: LiveData<Expense> = _expenseData

    var clickedButton by mutableStateOf(false)

    fun addingExpense(){
        clickedButton = true
    }
    fun finishingExpense(){
        clickedButton = false
    }
    fun addExpense(expense: Expense){
        viewModelScope.launch {
            try {
                expenseRepository.insertExpense(expense)
            }catch (e:Exception){
            }
        }
    }

    fun getAllExpenses(){
        viewModelScope.launch {
            try {
                expenseRepository.getExpenseStream()
            }catch (e:Exception){

            }
        }
    }


}