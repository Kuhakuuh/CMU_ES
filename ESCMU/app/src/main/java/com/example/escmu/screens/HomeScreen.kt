@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.escmu.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.escmu.components.AddExpense
import com.example.escmu.components.OverviewCards
import com.example.escmu.viewmodels.AppViewModelProvider
import com.example.escmu.viewmodels.HomeViewModel
import com.example.escmu.R
import com.example.escmu.Screens
import com.example.escmu.database.models.Expense
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
){

    val local = rememberSaveable { mutableStateOf("") }
    val value = rememberSaveable { mutableStateOf("") }
    val data = rememberSaveable { mutableStateOf("") }
    val expenses by viewModel.expenseData.observeAsState(initial = emptyList())


    Box(modifier = Modifier.fillMaxSize()){
            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                //Mudar para as funções que vao ser criadas
                OverviewCards(expense = 23, revenue =32 )
                Text(text = "Recent expenses",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp)
                )
                if(expenses.isNotEmpty()){
                    Column {
                        expenses.forEach{
                                expense ->
                            ExpenseItem(
                                expense = expense,
                                {}
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
            if (viewModel.clickedButton){
                AddExpenseDialog(

                    local = local.value,
                    data = data.value,
                    value =value.value ,
                    userID = "1",
                    onLocalChange = {local.value =it},
                    onValueChange ={value.value = it} ,
                    onDataChange ={data.value = it} ,
                    onClick ={
                        viewModel.addExpense(
                            Expense(
                                name="Expense",
                                idUser = "",
                                idGroup = "1",
                                image = "",
                                date = data.value,
                                value = value.value,
                                place = local.value))}
                    ,onDismiss ={ viewModel.finishingExpense()})
            }
        }

}


@Composable
fun ExpenseItem(expense: Expense,onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .aspectRatio(3f)
            .padding(8.dp)
            .clickable { onClick },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {

        //Text(text = "${expense.id}")
        Box(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.CenterHorizontally)) {
            Row(modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
                .align(Alignment.Center)
            ) {
                Text(text = "${expense.name}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.padding(6.dp))
                Text(text = "${expense.date}")
                Spacer(modifier = Modifier.padding(6.dp))
                Text(text = "${expense.value} €", fontWeight = FontWeight.ExtraBold)
            }


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
    local:String,
    data:String,
    value:String,
    userID:String,
    onLocalChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onDataChange: (String) -> Unit,
    onClick: (String) -> Unit,
    onDismiss:()->Unit){
    Dialog(onDismissRequest = { onDismiss()}) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val context = LocalContext.current
                    Column(
                        modifier = Modifier.padding(5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(text = "Register expense", fontWeight = FontWeight.Bold, style = TextStyle(fontSize = 40.sp))

                        Spacer(modifier = Modifier.height(20.dp))
                        TextField(
                            label = { Text(text = "value") },
                            value = value,
                            onValueChange = onValueChange
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        TextField(
                            label = { Text(text = "Local") },
                            value = local,
                            onValueChange = onLocalChange
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        TextField(
                            label = { Text(text = "Data") },
                            value = data,
                            onValueChange = onDataChange
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                            Button(
                                onClick = {
                                    if (local.isNotBlank()) {
                                        onClick(local)
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
                    Button(onClick = {onDismiss() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription ="Leave" )
                        Text(text = "Leave Dialog")

                    }
                }
            }
    }
}
