package com.example.escmu.viewmodels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.escmu.database.models.Expense
import com.example.escmu.database.models.User
import com.example.escmu.database.repository.ExpenseRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val expenseRepository:ExpenseRepository
):ViewModel() {

    private val _expenseData = MutableLiveData<List<Expense>>()
    val expenseData: LiveData<List<Expense>> = _expenseData

    var clickedButton by mutableStateOf(false)


    init {
        getAllExpenses()
    }

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

    private fun getAllExpenses(){
        viewModelScope.launch {
            try {
                expenseRepository.getExpenseStream().collect{
                    expenses ->
                    if (expenses.isNotEmpty()){
                      _expenseData.value = expenses
                    }
                }
            }catch (e:Exception){

            }
        }
    }


    fun deleteExpense(expense: Expense){
        viewModelScope.launch {
            try {
                expenseRepository.deleteExpense(expense)
                expenseRepository.getExpenseStream()
            }catch (e:Exception){
                Log.d("Expense","Fail deleting expense")
            }
        }
    }




}