package com.example.escmu.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.escmu.database.models.User
import com.example.escmu.viewmodels.AppViewModelProvider
import com.example.escmu.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
    ){


    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Profile Screen")

        val currentUser = viewModel.userData.value
        viewModel.getUser()
        if (currentUser != null) {
            Text(text = currentUser.name)
        }

        Button(
            onClick =
            {
                val user1 = User(name = "novo1",
                    id = "12",
                    email = "teste@gmail.com",
                    password = "fsfsf",
                    uuid = "1f3dfs3")
                viewModel.addUser(user1).also { viewModel.getUser() }
            }) {
            Text(text = "Add user")
        }
        if (currentUser != null) {
            UserInfo(currentUser)
        }


    }





}

@Composable
fun UserInfo(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}