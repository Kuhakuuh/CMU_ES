package com.example.escmu.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.escmu.Screens
import com.example.escmu.WindowSize
import com.example.escmu.components.SinglePhotoPicker
import com.example.escmu.components.TabRowProfile
import com.example.escmu.database.models.Expense
import com.example.escmu.database.models.User
import com.example.escmu.viewmodels.AppViewModelProvider
import com.example.escmu.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    windowSize: WindowSize,
    navController: NavController,
    viewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
    ){
    
    viewModel.getUser()
    viewModel.cleanExpenseList()
    val currentUser = viewModel.userData.value
    if (currentUser != null) {
        viewModel.getUserExpenses(currentUser.id)
    }

    var periodIndex by remember {
        mutableStateOf(0)
    }
    val periodLabels = listOf(
        "Info",
        "MyExpenses",
    )

    Column {
        // TabRow com as abas
        TabRow(
            selectedTabIndex = periodIndex,
            indicator = { tabPositions ->
                if (periodIndex < tabPositions.size) {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[periodIndex]),
                        shape = RoundedCornerShape(
                            topStart = 3.dp,
                            topEnd = 3.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp,
                        ),
                    )
                }
            },
        ) {
            periodLabels.forEachIndexed { index, title ->
                Tab(
                    selected = periodIndex == index,
                    onClick = {
                        periodIndex = index
                    },
                    text = {
                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                )
            }
        }
        // Conteúdo baseado na aba selecionada
        when (periodIndex) {
            0 -> {
                if (currentUser==null){
                    Button(onClick = { navController.navigate(Screens.Login.screen)}) {
                        Text(text = "Login")
                    }
                }

                if (currentUser != null) {
                    ProfileInfo(currentUser = currentUser, viewModel = viewModel, navController = navController)
                }
            }
            1 -> {
                ProfileMyExpenses(viewModel,navController)
            }
        }
    }

}

@Composable
fun ProfileInfo(currentUser:User,viewModel: UserViewModel,navController: NavController){


    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        if (currentUser != null) {
            UserInfo(currentUser)
        }

        Button(onClick = {
            try {
                viewModel.auth.signOut()
                viewModel.deleteAllUsers()
                navController.navigate(Screens.Login.screen)

            }catch (e:Exception){

            }
        }) {
            Text(text = "Terminar Sessão")
        }

    }
}

@Composable
fun ProfileMyExpenses(viewModel: UserViewModel,navController: NavController){
    viewModel.expenseList.forEach{ expense ->
        ExpenseItem(
            expense = expense,
            navController, modifier = Modifier.aspectRatio(3f)
        )
    }

}

@Composable
fun UserInfo(user: User) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Icon(
            Icons.Outlined.Person,
            contentDescription = "icon",
            modifier = Modifier.size(100.dp))

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