package com.example.escmu.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.escmu.Screens

data class NavigationItems(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String = ""
) {
    //function to get the list of navigationsItems
    fun navigationBarItems() : List<NavigationItems> {
        return listOf(
            NavigationItems(
                label = "Home",
                icon = Icons.Filled.Home,
                route = Screens.Home.screen
            ),
            NavigationItems(
                label = "Groups",
                icon = Icons.Filled.List,
                route = Screens.Groups.screen
            ),
            NavigationItems(
                label = "Profile",
                icon = Icons.Filled.Person,
                route = Screens.Profile.screen
            )


        )
    }
}