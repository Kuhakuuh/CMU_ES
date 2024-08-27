package com.example.escmu.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escmu.database.models.User
import com.example.escmu.database.repository.UserRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
):ViewModel() {
    private val _userData = MutableLiveData<User>()
    val userData:LiveData<User> = _userData

    fun addUser(user:User){
        viewModelScope.launch {
            try {
                userRepository.deleteAll()
                userRepository.insertUser(user)
            } catch (e:Exception){

            }
        }
    }
    fun getUser(){
        viewModelScope.launch {
            try {
                userRepository.getUserStream()
                    .collect{
                        user ->
                        if (user.isNotEmpty()){
                            _userData.value=user.first()
                        }
                    }
            }catch (e:Exception){

            }
        }
    }
    fun deleteUser(user: User){
        viewModelScope.launch {
            try {
                userRepository.deleteUser(user)
            }catch (e:Exception){

            }
        }
    }
    fun deleteAllUsers(){
        viewModelScope.launch {
            try {
                userRepository.deleteAll()
            }catch (e:Exception){

            }
        }
    }




}