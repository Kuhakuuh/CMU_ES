package com.example.escmu.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.escmu.components.GetCurrentLocation
import com.example.escmu.database.models.Expense
import com.example.escmu.database.models.User
import com.example.escmu.database.repository.ExpenseRepository
import com.example.escmu.database.repository.OfflineUserRepository
import com.example.escmu.database.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.util.UUID

class HomeViewModel(
    private val expenseRepository:ExpenseRepository,
    private val userRepository: UserRepository,
):ViewModel() {

    private val _expenseData = MutableLiveData<List<Expense>>()
    val expenseData: LiveData<List<Expense>> = _expenseData

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var clickedButton by mutableStateOf(false)
    var selectedImage by  mutableStateOf<Uri?>(null)

    private var _inspectedExpense = MutableLiveData<Expense>()
    var inspectedExpense:LiveData<Expense> = _inspectedExpense

    var isLoading by mutableStateOf(true)
    var isRefreshing by mutableStateOf(false)
    var selectedGroup by mutableStateOf("")
    var loadingExpenses by mutableStateOf(false)
    var group = MutableLiveData("")


    init {
        getExpenseFromFirebase()
        getAllExpenses()

    }


    fun addingExpense(){
        clickedButton = true
    }
    fun finishingExpense(){
        clickedButton = false
    }

    fun addExpense(expense: Expense,context: Context){
        viewModelScope.launch {
            try {
                finishingExpense()
                loadingExpenses=true
               addExpenseToFirebase(expense,context)
                getAllExpenses()

            }catch (e:Exception){
            }
        }
    }

    private fun getUserGroup(){
        viewModelScope.launch {
            try {
                userRepository.getUserStream()
                    .collect{
                            user ->
                        if (user.isNotEmpty()){
                           group.value= user.first().group
                        }
                    }
            }catch (e:Exception){

            }
        }
    }
     fun getExpenseFromFirebase(){
        getUserGroup()
         group.value
        viewModelScope.launch {
            expenseRepository.deleteAllExpenses()
            delay(1000)
        }

        val firestore = FirebaseFirestore.getInstance()
        val expenseCollection = firestore.collection("Expenses")
        expenseCollection
            .get()
            .addOnSuccessListener { document ->
                document.forEach {
                    document->
                    val expense = document.data?.let { parseExpense(it) }
                    viewModelScope.launch {
                        if (expense != null){
                            if (expense.idGroup == group.value){
                                expenseRepository.insertExpense(expense)
                                Log.d("Expense","expense add with success")
                            }

                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao buscar usuário por e-mail: $exception")
            }
    }

    private fun getExpenseFromFirebaseByGroup(group:String){

        viewModelScope.launch {
            expenseRepository.deleteAllExpenses()
        }

        val firestore = FirebaseFirestore.getInstance()
        val expenseCollection = firestore.collection("Expenses")
        expenseCollection
            .whereEqualTo("idGroup", group)
            .get()
            .addOnSuccessListener { document ->
                document.forEach {
                        document->
                    val expense = document.data?.let { parseExpense(it) }
                    viewModelScope.launch {
                        if (expense != null){
                            expenseRepository.insertExpense(expense)
                            Log.d("Expense","expense add with success")
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao buscar usuário por e-mail: $exception")
            }
    }

     fun getExpenseFromFirebaseById(id:String){

        val firestore = FirebaseFirestore.getInstance()
        val expenseCollection = firestore.collection("Expenses")
        expenseCollection.get()
            .addOnSuccessListener { document ->
                document.forEach {
                        document->
                    val expense = document.data?.let { parseExpense(it) }
                    viewModelScope.launch {
                        if (expense != null){
                            if (expense.id==id){
                                _inspectedExpense.value=expense
                            }
                            Log.d("Expense","expense add with success")
                            isLoading = false
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting user by e-mail: $exception")
            }
    }
    private fun parseExpense(expenseData: Map<String, Any>): Expense {
        return Expense(
            id = expenseData["id"] as? String ?: "",
            name = expenseData["name"] as? String ?: "",
            place = expenseData["place"] as? String ?: "",
            lat = expenseData["lat"] as? String ?: "",
            lng = expenseData["lng"] as? String ?: "",
            date = expenseData["date"] as? String ?: "",
            value = expenseData["value"] as? String ?: "",
            username = expenseData["username"] as? String ?: "",
            phonenumber = expenseData["phonenumber"] as? String ?: "",
            image = expenseData["image"] as? String ?: "",
            idUser = expenseData["idUser"] as? String ?: "",
            idGroup = expenseData["idGroup"] as? String ?: "")
        }
    private fun addExpenseToFirebase(expense: Expense, context: Context){
        loadingExpenses = true
        viewModelScope.launch {
            try {

                var storageRef =
                    FirebaseStorage.getInstance().getReference().child("images/${expense.id}.jpg");
                storageRef.putFile(selectedImage!!)
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener { url ->
                                var imgUrl = url.toString()
                                expense.image=imgUrl
                                Log.d("IMGURL", imgUrl)

                                val expenseFirestone = hashMapOf(

                                    "id" to expense.id,
                                    "name" to expense.name,
                                    "date" to expense.date  ,
                                    "value" to expense.value,
                                    "place" to expense.place,
                                    "lat" to expense.lat,
                                    "lng" to expense.lng,
                                    "username" to expense.username,
                                    "phonenumber" to expense.phonenumber,
                                    "image" to expense.image,
                                    "idUser" to expense.idUser,
                                    "idGroup" to expense.idGroup,
                                    )

                                firestore.collection("Expenses")
                                    .document(expense.id)
                                    .set(expenseFirestone)

                                //getExpenseFromFirebaseByGroup(expense.idGroup)
                                getExpenseFromFirebase()


                                Toast.makeText(
                                    context,
                                    "Upload successes",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        loadingExpenses = false

                    }

            }catch (e:Exception){
                Log.e("Firestoe","Erro adding expense!")

            }
        }
    }

     fun getAllExpenses(){
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

    fun getExpenseByUser(idUser:String){
        val firestore = FirebaseFirestore.getInstance()
        val expenseCollection = firestore.collection("Expenses")
        expenseCollection.get()
            .addOnSuccessListener { document ->
                document.forEach {
                        document->
                    val expense = document.data?.let { parseExpense(it) }
                    viewModelScope.launch {
                        if (expense != null){
                            if (expense.idUser==idUser){
                                _inspectedExpense.value=expense
                            }
                            Log.d("Expense","expense add with success")
                            isLoading = false
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting user by e-mail: $exception")
            }
    }

    fun getExpenseByGroup(group:String){
        val firestore = FirebaseFirestore.getInstance()
        val expenseCollection = firestore.collection("Expenses")
        expenseCollection.get()
            .addOnSuccessListener { document ->
                document.forEach {
                        document->
                    val expense = document.data?.let { parseExpense(it) }
                    viewModelScope.launch {
                        if (expense != null){
                            if (expense.idGroup==group){
                                _inspectedExpense.value=expense
                            }
                            Log.d("Expense","expense add with success")
                            isLoading = false
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting user by e-mail: $exception")
            }
    }



}