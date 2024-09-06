package com.example.escmu.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escmu.database.models.Expense
import com.example.escmu.database.models.Group
import com.example.escmu.database.models.User
import com.example.escmu.database.repository.ExpenseRepository
import com.example.escmu.database.repository.GroupRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GroupViewModel(
    private val groupRepository: GroupRepository
):ViewModel() {

    private val _groupData = MutableLiveData<List<Group>>()
    val groupData: LiveData<List<Group>> = _groupData
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    var isLoading by mutableStateOf(true)
    val userList = mutableStateListOf<User>()

    init {
        loadGroupsFromFirebase()
        getGroups()
    }

    fun getUsersByGroup(group: String){
        userList.clear()
        val firestore = FirebaseFirestore.getInstance()
        val expenseCollection = firestore.collection("Users")
        expenseCollection
            .whereEqualTo("group",group)
            .get()
            .addOnSuccessListener {document->
                document.forEach {
                        document->
                    val user = document.data?.let { parseUser(it) }
                    viewModelScope.launch {
                        if (user != null){
                         //   if (user.group== group){
                                userList.add(user)
                           // }
                            Log.d("Group","group add with success")
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Group", "Error finding groups: $exception")
            }


    }


    private fun addGroupToFirebase(group: Group){
        val groupFirestone = hashMapOf(
            "id" to group.id,
            "name" to group.name,
            "totalValue" to group.totalValue,
        )

        viewModelScope.launch {
            firestore.collection("Groups")
                .document(group.name)
                .set(groupFirestone)
                .addOnSuccessListener { documentReference ->
                    Log.d("Firestore", "Group add with success : ${group.id}")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Erro ao adicionar user: ${e.message}", e)
                }.await()
        }

    }


    fun addGroup(group: Group){
        viewModelScope.launch {
            try {
                addGroupToFirebase(group)
                loadGroupsFromFirebase()

            }catch (e:Exception){
                Log.d("Group","Fail to add Groups")
            }
        }
    }

     fun loadGroupsFromFirebase(){
         viewModelScope.launch {
             groupRepository.deleteAllGroups()
             delay(1000)
         }
        val firestore = FirebaseFirestore.getInstance()
        val expenseCollection = firestore.collection("Groups")
        expenseCollection.get()
            .addOnSuccessListener { document ->
                document.forEach {
                        document->
                    val group = document.data?.let { parseGroup(it) }
                    viewModelScope.launch {
                        if (group != null){
                            groupRepository.insertGroup(group)
                            Log.d("Group","group add with success")
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Group", "Error finding groups: $exception")
            }
    }
    private fun parseGroup(groupData: Map<String, Any>): Group {
        return Group(
            id = groupData["id"] as? String ?: "",
            name = groupData["name"] as? String ?: "",
            totalValue = groupData["totalValue"] as? Double ?: 0.0)

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