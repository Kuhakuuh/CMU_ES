@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.escmu.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.escmu.Screens
import com.example.escmu.components.AddExpense
import com.example.escmu.components.CustomDatePicker
import com.example.escmu.components.DropDown
import com.example.escmu.components.GetCurrentLocation
import com.example.escmu.components.OverviewCards
import com.example.escmu.components.SinglePhotoPicker
import com.example.escmu.viewmodels.AppViewModelProvider
import com.example.escmu.viewmodels.HomeViewModel
import com.example.escmu.database.models.Expense
import com.example.escmu.database.models.Group
import com.example.escmu.retrofit.RetrofitHelper.getLocation
import com.example.escmu.viewmodels.GroupViewModel
import com.example.escmu.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    groupViewModel: GroupViewModel= viewModel(factory = AppViewModelProvider.Factory),
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
){

    if (FirebaseAuth.getInstance().currentUser == null){
        navController.navigate(Screens.Login.screen)
    }


    val name = rememberSaveable { mutableStateOf("") }
    val place = rememberSaveable { mutableStateOf("") }
    val value = rememberSaveable { mutableStateOf("") }
    val data = rememberSaveable { mutableStateOf("") }
    val currentUser = rememberSaveable { mutableStateOf("") }

    val expenses by viewModel.expenseData.observeAsState(initial = emptyList())
    val context = LocalContext.current
    val location = GetCurrentLocation()

    var isLoadingPlace by remember {mutableStateOf(true)}

    val user = userViewModel.userData.value
    if (user!=null){
        currentUser.value = user.id
    }
    viewModel.getExpenseFromFirebase()
    LaunchedEffect(isLoadingPlace ){
        if (location!=null){
            place.value = getLocation(location.first,location.second)?.address?.village ?: ""
            isLoadingPlace=false
        }
       }


    Box(modifier = Modifier.fillMaxSize()){
            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(text = "Teste user ${viewModel.group.value}")
                //Get current location: place
                if (location != null) {
                    LaunchedEffect(true ){
                        var address=getLocation(location.first,location.second)
                        if (address != null) {
                            place.value= address.address.village
                        }
                    }
                }

                //Mudar para as funções que vao ser criadas
                OverviewCards(expense = getTotalValue(viewModel), revenue ="0" )
                Text(text = "Recent group expenses",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp)
                )
                if(expenses.isNotEmpty()){
                    Column {
                        expenses.forEach{
                                expense ->
                            ExpenseItem(
                                expense = expense,
                                navController
                            )
                        }
                    }
                }else{
                    GhostCards()
                }

            }

            AddExpense(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
                onClick = {viewModel.addingExpense()})

        }

    if (viewModel.loadingExpenses) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center // Centraliza o conteúdo dentro do Box
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }

    if (viewModel.clickedButton){
        Box(modifier = Modifier.fillMaxSize()){

            AddExpenseDialog(
                name=name.value,
                value =value.value ,
                place=place.value,
                idUser = currentUser.value,
                homeViewModel = viewModel,
                groupViewModel = groupViewModel,
                onNameChange = {name.value=it},
                onValueChange ={value.value = it} ,
                onDataChange = {it},
                onClick ={
                    viewModel.addExpense(
                        Expense(
                            name=name.value,
                            idUser = currentUser.value,
                            idGroup =viewModel.selectedGroup,
                            image = "",
                            date = data.value,
                            value = value.value,
                            place = place.value),
                        context = context)
                         }
                ,onDismiss ={
                    viewModel.finishingExpense()
                    navController.navigate(Screens.Home.screen)
                })
        }
    }

}

fun getTotalValue(viewModel: HomeViewModel):String{
    var total =0.0
    viewModel.expenseData.value?.forEach {
            expense ->
        total += expense.value.toDouble()
    }
    return total.toString()
}

@Composable
fun ExpenseItem(expense: Expense,navController: NavController) {
    ElevatedCard(
        modifier = Modifier
            .aspectRatio(3f)
            .padding(8.dp)
            .clickable { navController.navigate("expenseDetail/${expense.id}") },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Text(
            text = "Username",
            fontWeight = FontWeight.Light,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.Start).padding(6.dp)
        )
        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Linha principal com o valor e o nome

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Valor
                Text(
                    text = "  - ${expense.value} €",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = Color.Red,

                )

                // Nome
                Text(
                    text = expense.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Top)
                )
            }
            // Data na parte inferior
            Text(
                text = expense.date,
                fontWeight = FontWeight.Light,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.End)
            )

        }
    }
    
}





@Composable
fun GhostCards(){
    Column {
        repeat(5){
            GhostItem()
        }
    }


}



@Composable
fun GhostItem() {
    val animationSpec: AnimationSpec<Float> = infiniteRepeatable(
        tween(
            durationMillis = 1000,
            easing = LinearEasing
        )
    )
    val alpha = remember { Animatable(0.1f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 0.2f,
            animationSpec = animationSpec
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = Color.LightGray.copy(alpha = alpha.value),
        shape = RoundedCornerShape(8.dp)
    ) {
        Spacer(
            modifier = Modifier
                .height(100.dp)
                .padding(16.dp)
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    name:String,
    place:String,
    value:String,
    idUser:String,
    homeViewModel: HomeViewModel,
    groupViewModel: GroupViewModel,
    onNameChange:(String) -> Unit,
    onValueChange: (String) -> Unit,
    onDataChange: (String) -> Unit,
    onClick: (String) -> Unit,
    onDismiss:()->Unit

){

    var groupList = groupViewModel.groupData.value
    var isExpanded by remember {mutableStateOf(false) }
    var selectText by remember { mutableStateOf( groupList?.get(0))}

            Dialog(onDismissRequest = { onDismiss() }) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer, // Ensure this is not transparent
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            val context = LocalContext.current
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = "Register expense",
                                    fontWeight = FontWeight.Bold,
                                    style = TextStyle(fontSize = 40.sp)
                                )


                                OutlinedTextField(
                                    label = { Text(text = "User") },
                                    value = idUser,
                                    onValueChange = { },
                                    readOnly = true
                                )

                                OutlinedTextField(
                                    label = { Text(text = "Name") },
                                    value = name,
                                    onValueChange = onNameChange
                                )

                                Spacer(modifier = Modifier.height(20.dp))
                                OutlinedTextField(
                                    label = { Text(text = "Place") },
                                    value = place,
                                    onValueChange = onNameChange,
                                    readOnly = true
                                )

                                Spacer(modifier = Modifier.height(20.dp))
                               var data =CustomDatePicker()
                                OutlinedTextField(
                                    label = { Text(text = "Data") },
                                    value = data,
                                    onValueChange = onDataChange,
                                    readOnly = true
                                )

                                Spacer(modifier = Modifier.height(20.dp))


                                //Selecionar grupo
                                ExposedDropdownMenuBox(
                                    expanded = isExpanded,
                                    onExpandedChange = { isExpanded = !isExpanded }
                                ) {
                                    OutlinedTextField(
                                        modifier = Modifier.menuAnchor(),
                                        value = selectText?.name ?: "",
                                        onValueChange = {
                                            homeViewModel.selectedGroup = selectText?.name ?: ""
                                        },
                                        readOnly = true,
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                expanded = isExpanded
                                            )
                                        }
                                    )
                                    ExposedDropdownMenu(
                                        expanded = isExpanded,
                                        onDismissRequest = { isExpanded = false }) {
                                        groupList?.forEachIndexed { index, text ->
                                            DropdownMenuItem(
                                                text = { Text(text = text.name) },
                                                onClick = {
                                                    selectText = groupList[index]
                                                    isExpanded = false
                                                    homeViewModel.selectedGroup =
                                                        selectText?.name ?: ""
                                                },
                                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                                OutlinedTextField(
                                    label = { Text(text = "Value") },
                                    value = value,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal
                                    ),
                                    onValueChange = onValueChange
                                )
                                Spacer(modifier = Modifier.height(20.dp))

                                homeViewModel.selectedImage = SinglePhotoPicker()

                                Spacer(modifier = Modifier.height(20.dp))

                                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                                    Button(
                                        onClick = {
                                            if (name.isNotBlank()) {
                                                onClick(name)
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Please enter an local",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        },
                                        shape = RoundedCornerShape(50.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                    ) {
                                        Text(text = "Register expense")
                                    }
                                }
                            }

                            Text(text = "This is a full-screen dialog")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { onDismiss() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Leave"
                                )
                                Text(text = "Leave Dialog")

                            }
                        }
                    }
                }
            }
}
