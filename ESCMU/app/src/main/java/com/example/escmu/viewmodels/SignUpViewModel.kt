package com.example.escmu.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escmu.database.models.User
import com.example.escmu.database.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpViewModel(
    private val userRepository: UserRepository,
):ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var context: Context



    fun createAccount(email: String, password: String) {
        viewModelScope.launch {
            runCatching {
                val authResult = withContext(Dispatchers.IO) {
                    auth.createUserWithEmailAndPassword(email, password).await()
                }
            }
                .onSuccess {
                    Log.i("TAG", "Success")
                }
        }
    }

    fun addUserToFirestore(user: User) {
        val auth = FirebaseAuth.getInstance()
        val email = auth.currentUser ?: auth.currentUser?.email

        val userFirestone = hashMapOf(
            "id" to user.id,
            "name" to user.name,
            "email" to user.email  ,
            "password" to user.password,
        )
        viewModelScope.launch {
            firestore.collection("Users")
                .document(user.email)
                .set(userFirestone)
                .addOnSuccessListener { documentReference ->
                    Log.d("Firestore", "User adicionado com sucesso: ${user.id}")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Erro ao adicionar user: ${e.message}", e)
                }.await()
        }

    }



}