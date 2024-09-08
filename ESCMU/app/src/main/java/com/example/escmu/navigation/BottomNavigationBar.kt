package com.example.escmu.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.escmu.Screens
import com.example.escmu.WindowSize
import com.example.escmu.screens.ExpenseDetail
import com.example.escmu.screens.GroupDetail
import com.example.escmu.screens.GroupsScreen
import com.example.escmu.screens.HomeScreen
import com.example.escmu.screens.LoginScreen
import com.example.escmu.screens.ProfileScreen
import com.example.escmu.screens.SignUpScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(windowSize: WindowSize) {

    var navigationSelectedItem by remember {
        mutableStateOf(0)
    }

    /**
     * by using the rememberNavController()
     * we can get the instance of the navController
     */
    val navController = rememberNavController()

//scaffold to hold our bottom navigation Bar
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                //getting the list of bottom navigation items for our data class
                NavigationItems().navigationBarItems()
                    .forEachIndexed { index, navigationItem ->
                        //iterating all items with their respective indexes
                        NavigationBarItem(
                            selected = index == navigationSelectedItem,
                            label = {
                                Text(navigationItem.label)
                            },
                            icon = {
                                Icon(
                                    navigationItem.icon,
                                    contentDescription = navigationItem.label
                                )
                            },
                            onClick = {
                                navigationSelectedItem = index
                                navController.navigate(navigationItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home.screen,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {


            composable(Screens.Home.screen) {
                //call our composable screens here
                HomeScreen( windowSize,navController = navController)
                //HomeScreenPage(navController = navController)
            }
            composable(Screens.Profile.screen) {
                //call our composable screens here
                ProfileScreen( windowSize,navController = navController)
            }
            composable(Screens.Login.screen) {
                LoginScreen( windowSize,navController = navController)
            }
            composable(Screens.SignUp.screen) {
                SignUpScreen( windowSize,navController = navController)
            }
            composable(Screens.Groups.screen) {
                GroupsScreen( windowSize,navController = navController)
            }
            composable("expenseDetail/{expenseID}"){ backStackEntry ->
                val expenseID = backStackEntry.arguments?.getString("expenseID")
                if (expenseID != null) {
                    ExpenseDetail(windowSize,expenseId = expenseID,navController)
                }
            }
            composable("groupDetail/{group}"){ backStackEntry ->
                val groupID = backStackEntry.arguments?.getString("group")
                if (groupID != null) {
                    GroupDetail(windowSize,group = groupID,navController)
                }
            }

        }
    }
}