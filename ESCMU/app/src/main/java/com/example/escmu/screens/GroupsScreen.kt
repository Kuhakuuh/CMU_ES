@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.escmu.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.escmu.Screens
import com.example.escmu.database.models.Group
import com.example.escmu.viewmodels.AppViewModelProvider
import com.example.escmu.viewmodels.GroupViewModel
import com.example.escmu.viewmodels.HomeViewModel
import com.example.escmu.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun GroupsScreen(
    navController: NavController,
    viewModel: GroupViewModel = viewModel(factory = AppViewModelProvider.Factory),
    homeViewModel:HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    if (FirebaseAuth.getInstance().currentUser == null){
        navController.navigate(Screens.Login.screen)
    }


    val name = rememberSaveable { mutableStateOf("") }
    val groups by viewModel.groupData.observeAsState(initial = emptyList())
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            GroupFrom(
                name = name.value,
                onNameChange = {name.value = it},
                onClick = {viewModel.addGroup(
                    Group(
                        name = name.value,
                        totalValue = 0.0,
                        )
                )}
            )
            Text(
                text = "Available groups",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
            )

            Column {
                groups.forEach{
                    group ->
                    GroupItem(
                        group = group,
                        navController,
                        userViewModel
                    )
                }
            }
            
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupFrom(
    name:String,
    onNameChange: (String) -> Unit,
    onClick: (String) -> Unit
){
    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Create a new group", fontWeight = FontWeight.Bold, style = TextStyle(fontSize = 40.sp))
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            label = { Text(text = "Name") },
            value = name,
            onValueChange = onNameChange
        )

        Button(modifier = Modifier.padding(6.dp),
            onClick = {
            if (name.isNotBlank()) {
                onClick(name)
            } else {
                Toast.makeText(
                    context,
                    "Please enter a name",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }) {
            Text(text = "Create group")
        }
    }
}
@Composable
fun GroupItem(group: Group,navController: NavController,userViewModel: UserViewModel){
    ElevatedCard(
        modifier = Modifier
            .aspectRatio(2f)
            .padding(8.dp)
            .clickable { navController.navigate("groupDetail/${group.name}") },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center, // Centraliza o conteúdo no meio do card
            modifier = Modifier.fillMaxSize() // Garante que o Box ocupe todo o espaço do card
        ) {
            Text(
                text = group.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
            )
        }


    }
    
}
