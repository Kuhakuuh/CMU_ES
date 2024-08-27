package com.example.escmu.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.escmu.components.AddExpense
import com.example.escmu.components.OverviewCards
import com.example.escmu.viewmodels.AppViewModelProvider
import com.example.escmu.viewmodels.HomeViewModel
import com.example.escmu.R

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
){

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


            }

            AddExpense(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
                onClick = {viewModel.addingExpense()})
            if (viewModel.clickedButton){
                AddExpenseDialog(onDismiss ={ viewModel.finishingExpense()})
            }
        }
}


@Composable
fun AddExpenseDialog(onDismiss:()->Unit){
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
                    Text(text = "This is a full-screen dialog")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {onDismiss() }) {
                        Text(text = "Leave Dialog")
                        
                    }
                }
            }
    }
}
