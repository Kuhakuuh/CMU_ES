package com.example.escmu

sealed class Screens(val screen:String) {
     object Home: Screens("home")
     object Profile:Screens("profile")
     object Login:Screens("login")
     object SignUp:Screens("signup")
     object Groups:Screens("groups")
     object ExpenseDetail:Screens("expense")


}