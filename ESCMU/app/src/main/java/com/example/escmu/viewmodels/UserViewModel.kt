package com.example.escmu.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escmu.database.models.User
import com.example.escmu.database.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
):ViewModel() {
    private val _userData = MutableLiveData<User>()
    val userData:LiveData<User> = _userData

    var auth: FirebaseAuth= FirebaseAuth.getInstance()


    init{
        getUser()
    }

    fun getUserByEmail(email: String) {
        val firestore = FirebaseFirestore.getInstance()
        val usersCollection = firestore.collection("Users").document(email)

        usersCollection.get()
            .addOnSuccessListener { document ->

                val user = document.data?.let { parseUser(it) }

                viewModelScope.launch {
                    if (user != null) {
                        userRepository.insertUser(user)
                    }
                }
            }
            .addOnFailureListener { exception ->

                Log.e("Firestore", "Erro ao buscar usuário por e-mail: $exception")
            }
    }

    private fun parseUser(userData: Map<String, Any>): User {
        return User(
            id = userData["id"] as? String ?: "",
            name = userData["name"] as? String ?: "",
            email = userData["email"] as? String ?: "",
            password = userData["password"] as? String ?: "",

        )
    }
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