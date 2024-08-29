package com.example.escmu.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escmu.database.models.Expense
import com.example.escmu.database.models.Group
import com.example.escmu.database.repository.ExpenseRepository
import com.example.escmu.database.repository.GroupRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GroupViewModel(
    private val groupRepository: GroupRepository
):ViewModel() {

    private val _groupData = MutableLiveData<List<Group>>()
    val groupData: LiveData<List<Group>> = _groupData

    init {
        getGroups()
    }

    fun addGroup(group: Group){
        viewModelScope.launch {
            try {
                groupRepository.insertGroup(group)

            }catch (e:Exception){
                Log.d("Group","Fail to add Groups")
            }
        }
    }

    private fun getGroups(){
        viewModelScope.launch {
            try {
                groupRepository.getGroupStream().collect{
                    groups ->
                    if (groups.isNotEmpty()){
                        _groupData.value = groups
                    }
                }

            }catch (e:Exception){
                Log.d("Group","Fail to load Groups")
            }

        }

    }





}