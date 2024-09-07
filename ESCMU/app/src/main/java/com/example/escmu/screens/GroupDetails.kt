package com.example.escmu.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.escmu.Screens
import com.example.escmu.database.models.User
import com.example.escmu.viewmodels.AppViewModelProvider
import com.example.escmu.viewmodels.GroupViewModel
import com.example.escmu.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun GroupDetail(
    group:String,
    navController: NavController,
    groupViewModel: GroupViewModel = viewModel(factory = AppViewModelProvider.Factory),
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    if (FirebaseAuth.getInstance().currentUser == null){
        navController.navigate(Screens.Login.screen)
    }

    LaunchedEffect(groupViewModel.isLoading) {
        groupViewModel.getUsersByGroup(group)
        if (groupViewModel.isLoading) {
            delay(1000)
            groupViewModel.isLoading = false
        }
    }
    var user = userViewModel.userData.value
    var members = groupViewModel.userList.toList()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Group:  $group",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(text = "Group Members",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(5.dp)
        )
        members.forEach { user ->
            MemberItem(user)
        }


        Row {
            if (user != null) {
                if(user.group == ""){
                    Button(
                        onClick ={
                            FirebaseAuth.getInstance().currentUser?.email?.let {
                        userViewModel.addGroup(
                            it,group)}

                            navController.navigate(Screens.Groups.screen)
                        }
                    ) {
                        Text(text = "Enter group")

                    }
                } else{
                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().currentUser?.email?.let {
                        userViewModel.leaveGroup(
                            it)
                    }
                            navController.navigate(Screens.Groups.screen)
                        }) {
                        Text(text = "Leave group")

                    }
                }

            }

        }
        
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Close group")
            
        }

    }


}
@Composable
fun MemberItem(user:User){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Text(
            text = user.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }


}