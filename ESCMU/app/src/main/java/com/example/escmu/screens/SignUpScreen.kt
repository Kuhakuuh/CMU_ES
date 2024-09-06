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
import com.example.escmu.database.models.User
import com.example.escmu.viewmodels.AppViewModelProvider
import com.example.escmu.viewmodels.SignUpViewModel
import com.example.escmu.viewmodels.UserViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    signUpViewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory),
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)

){
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val username = rememberSaveable { mutableStateOf("") }
    val name = rememberSaveable { mutableStateOf("") }


    RegisterForm( navController, email = email.value, password = password.value,name=name.value, username = username.value,
        onEmailChange = { email.value = it },
        onPasswordChange = { password.value = it },
        onUsernameChange = { username.value = it },
        onNameChange = { name.value = it },
        onRegisterClick = {
            try {
                signUpViewModel.createAccount(email.value,password.value)
                signUpViewModel.addUserToFirestore(User(name=name.value, password = password.value, email = email.value, group = ""))
                userViewModel.addUser(User(name=name.value, password = password.value, email = email.value, group = ""))
                navController.navigate(Screens.Home.screen)

            }catch (e:Exception){
                Log.d("SignUp","Fail to signUp")
            }

        }
    )


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterForm(
    navController: NavController,
    email: String,
    password: String,
    name: String,
    username:String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onRegisterClick: (String) -> Unit
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(
            text = AnnotatedString("Login here"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = { navController.navigate(Screens.Login.screen){popUpTo(0)} },
            style = TextStyle(
                fontSize = 14.sp,

                textDecoration = TextDecoration.Underline,

                )
        )
    }

    Column(
        modifier = Modifier.padding(20.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "SignUp", fontWeight = FontWeight.Bold, style = TextStyle(fontSize = 40.sp))
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Username") },
            value = username,
            onValueChange = onUsernameChange
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Name") },
            value = name,
            onValueChange = onNameChange
        )

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
                        onRegisterClick(email)
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
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Register")
            }
        }

    }
}