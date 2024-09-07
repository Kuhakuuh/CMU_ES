package com.example.escmu.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escmu.database.models.Expense
import com.example.escmu.database.models.Group
import com.example.escmu.database.models.User
import com.example.escmu.database.repository.ExpenseRepository
import com.example.escmu.database.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel(
    private val userRepository: UserRepository,
    private val expenseRepository: ExpenseRepository
):ViewModel() {


    private val _userData = MutableLiveData<User>()
    val userData:LiveData<User> = _userData
    var auth: FirebaseAuth= FirebaseAuth.getInstance()
    val expenseList = mutableStateListOf<Expense>()

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
    fun getUserById(id: String) {
        val firestore = FirebaseFirestore.getInstance()
        val usersCollection = firestore
            .collection("Users")
            .whereEqualTo("id",id)
        usersCollection
            .get()
            .addOnSuccessListener { document ->
                document.forEach{
                        document->
                    val user = document.data?.let { parseUser(it) }
                    if (user != null) {

                    }

                }
            }
            .addOnFailureListener { exception ->

                Log.e("Firestore", "Erro ao buscar usuário por e-mail: $exception")
            }
    }
    fun cleanExpenseList(){
        expenseList.clear()
    }
    fun getUserExpenses(id: String) {
        cleanExpenseList()
        val firestore = FirebaseFirestore.getInstance()
        val usersCollection = firestore
            .collection("Expenses")
            .whereEqualTo("idUser",id)
        usersCollection
            .get()
            .addOnSuccessListener { document ->
                document.forEach{
                        document->
                    val expense = document.data?.let { parseExpense(it) }
                    if (expense != null) {

                        expenseList.add(expense)
                    }

                }
            }
            .addOnFailureListener { exception ->

                Log.e("Firestore", "Erro ao buscar usuário por e-mail: $exception")
            }
    }

    private fun parseExpense(expenseData: Map<String, Any>): Expense {
        return Expense(
            id = expenseData["id"] as? String ?: "",
            name = expenseData["name"] as? String ?: "",
            username = expenseData["username"] as? String ?: "",
            place = expenseData["place"] as? String ?: "",
            lat = expenseData["lat"] as? String ?: "",
            lng = expenseData["lng"] as? String ?: "",
            date = expenseData["date"] as? String ?: "",
            value = expenseData["value"] as? String ?: "",
            image = expenseData["image"] as? String ?: "",
            idUser = expenseData["idUser"] as? String ?: "",
            idGroup = expenseData["idGroup"] as? String ?: "")
    }

    fun addGroup(email: String,group: String) {
        val firestore = FirebaseFirestore.getInstance()

        val userUpdates = hashMapOf(
            "group" to group,

        )
        viewModelScope.launch {
            firestore.collection("Users")
                .document(email as String)
                .update(userUpdates as Map<String, Any>)
                .addOnSuccessListener {
                    deleteAllUsers()
                    getUserByEmail(email)

                    Log.d("User", "User adicionado ao grupo $group")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Erro ao adicionar user: ${e.message}", e)
                }.await()
        }

    }
    fun leaveGroup(email: String) {
        val firestore = FirebaseFirestore.getInstance()

        val userUpdates = hashMapOf(
            "group" to "",

            )
        viewModelScope.launch {
            firestore.collection("Users")
                .document(email as String)
                .update(userUpdates as Map<String, Any>)
                .addOnSuccessListener {
                    deleteAllUsers()
                    getUserByEmail(email)
                    Log.d("User", "User saiu do grupo")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Erro ao adicionar user: ${e.message}", e)
                }.await()
            
        }

    }

    private fun parseUser(userData: Map<String, Any>): User {
        return User(
            id = userData["id"] as? String ?: "",
            name = userData["name"] as? String ?: "",
            email = userData["email"] as? String ?: "",
            password = userData["password"] as? String ?: "",
            group = userData["group"] as? String ?: "",

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