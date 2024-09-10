package com.example.escmu.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.escmu.Screens
import com.example.escmu.WindowSize
import com.example.escmu.viewmodels.AppViewModelProvider
import com.example.escmu.viewmodels.HomeViewModel
import com.example.escmu.viewmodels.LoginViewModel
import com.example.escmu.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    windowSize: WindowSize,
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory),
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }

    //Verifica se o user já esta login
    // val auth:FirebaseAuth=FirebaseAuth.getInstance()
    //if (auth.currentUser != null){
    //   navController.navigate(Screens.Home.screen)
    //}



        LoginForm( navController, email = email.value, password = password.value,
            onEmailChange = { email.value = it },
            onPasswordChange = { password.value = it },
            onLoginClick = {
                try {
                    loginViewModel.signIn(email.value,password.value)
                    userViewModel.getUserByEmail(email.value)
                    navController.navigate(Screens.Home.screen)

                }catch (e:Exception){
                    Log.d("Login","Fail to login")

                }
            }
        )



}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(
    navController: NavController,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: (String) -> Unit
) {
    val context = LocalContext.current


    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(
            text = AnnotatedString("Sign up here"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = { navController.navigate(Screens.SignUp.screen){popUpTo(0)} },
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.White,
                textDecoration = TextDecoration.Underline,

                )
        )
    }

    Column(
        modifier = Modifier.padding(20.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Login", fontWeight = FontWeight.Bold, style = TextStyle(fontSize = 40.sp))

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Email") },
            value = email,
            onValueChange = onEmailChange
        )

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Password") },
            value = password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = onPasswordChange
        )

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        onLoginClick(email)
                    } else {
                        Toast.makeText(
                            context,
                            "Please enter an email and password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
            ) {
                Text(text = "Login")
            }
        }
    }

}