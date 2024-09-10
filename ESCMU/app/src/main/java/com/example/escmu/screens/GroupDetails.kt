package com.example.escmu.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.escmu.Screens
import com.example.escmu.WindowSize
import com.example.escmu.components.MyService
import com.example.escmu.database.models.User
import com.example.escmu.viewmodels.AppViewModelProvider
import com.example.escmu.viewmodels.GroupViewModel
import com.example.escmu.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun GroupDetail(
    windowSize: WindowSize,
    group:String,
    navController: NavController,
    groupViewModel: GroupViewModel = viewModel(factory = AppViewModelProvider.Factory),
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var context = LocalContext.current

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

        Button(onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.CenterHorizontally) ) {
            Icon(
                imageVector = Icons.Sharp.KeyboardArrowLeft,
                contentDescription ="Back" )

        }
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
                            userViewModel.userData.value?.let { userViewModel.addGroup(it.email,group) }
                            userViewModel.getUserByEmail(email = user.email)
                            restartService(context)
                            navController.navigate(Screens.Groups.screen)
                        }
                    ) {
                        Text(text = "Enter group")

                    }
                } else{
                    Button(
                        onClick = {
                            userViewModel.userData.value?.let { userViewModel.leaveGroup(it.email) }
                            userViewModel.getUserByEmail(user.email)
                            restartService(context)
                            navController.navigate(Screens.Home.screen)
                        }) {
                        Text(text = "Leave group")

                    }
                }

            }

        }
        
        Button(
            onClick = {
                groupViewModel.removeGroupFromFirebase(groupId = group)
                navController.navigate(Screens.Groups.screen)

            }
        ) {
            Text(text = "Delete group")
            
        }

    }

}


fun restartService(context:Context){
    context.startService(Intent(context, MyService::class.java))
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