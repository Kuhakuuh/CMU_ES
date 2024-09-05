package com.example.escmu.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
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

    groupViewModel.getUsersByGroup(group)
    var user = userViewModel.userData.value
    var members = groupViewModel.userList


    LaunchedEffect(groupViewModel.isLoading) {
        if (groupViewModel.isLoading) {
            delay(1000)
            groupViewModel.isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        if (user != null) {
            Text(text = "USER:${user.email}")
        }
        members.forEach { user ->
            MemberItem(user)
        }
        Text(text = group)

        Button(onClick = { FirebaseAuth.getInstance().currentUser?.email?.let {
            userViewModel.addGroup(
                it,group)
        } }) {
            Text(text = "Enter group")

        }

        Button(onClick = { FirebaseAuth.getInstance().currentUser?.email?.let {
            userViewModel.leaveGroup(
                it)
        } }) {
            Text(text = "Leave group")

        }
    }


}
@Composable
fun MemberItem(user:User){
    Column() {

        Text(text = user.name)
    }
}