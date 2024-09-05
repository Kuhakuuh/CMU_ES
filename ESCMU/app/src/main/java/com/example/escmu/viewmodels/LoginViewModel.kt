package com.example.escmu.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escmu.database.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val userRepository: UserRepository
):ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            runCatching {
                val authResult = withContext(Dispatchers.IO) {
                    auth.signInWithEmailAndPassword(email, password).await()
                }
            }
                .onSuccess {
                    Log.i("TAG", "User login with success")
                }
        }

    }


}