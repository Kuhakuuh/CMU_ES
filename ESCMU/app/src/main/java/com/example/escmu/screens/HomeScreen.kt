@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.escmu.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.escmu.Screens
import com.example.escmu.WindowSize
import com.example.escmu.WindowType
import com.example.escmu.components.AddExpense
import com.example.escmu.components.CustomDatePicker
import com.example.escmu.components.GetCurrentLocation
import com.example.escmu.components.OverviewCards
import com.example.escmu.components.SinglePhotoPicker
import com.example.escmu.viewmodels.AppViewModelProvider
import com.example.escmu.viewmodels.HomeViewModel
import com.example.escmu.database.models.Expense
import com.example.escmu.retrofit.RetrofitHelper.getLocation
import com.example.escmu.viewmodels.GroupViewModel
import com.example.escmu.viewmodels.UserViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    windowSize: WindowSize,
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    groupViewModel: GroupViewModel = viewModel(factory = AppViewModelProvider.Factory),
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current

    //Get all expenses
    viewModel.getExpenseFromFirebase()
    viewModel.getAllExpenses()

    val name = rememberSaveable { mutableStateOf("") }
    val username = rememberSaveable { mutableStateOf("") }
    val phonenumber = rememberSaveable { mutableStateOf("") }
    val place = rememberSaveable { mutableStateOf("") }
    val value = rememberSaveable { mutableStateOf("") }
    val data = rememberSaveable { mutableStateOf("") }
    val currentUser = rememberSaveable { mutableStateOf("") }
    val lat = rememberSaveable { mutableStateOf("") }
    val lng = rememberSaveable { mutableStateOf("") }
    val expenses by viewModel.expenseData.observeAsState(initial = emptyList())
    val location = GetCurrentLocation()


    //SwipeRefresh

    shakeToRefresh(context,viewModel)
    LaunchedEffect(viewModel.isRefreshing) {
        if (viewModel.isRefreshing) {
            delay(2000)
            viewModel.isRefreshing = false
        }
    }


    if (location != null) {
        lat.value = location.first.toString()
        lng.value = location.second.toString()

    }
    var isLoadingPlace by remember { mutableStateOf(true) }

    val user = userViewModel.userData.value
    if (user != null) {
        currentUser.value = user.id
        username.value = user.name
        phonenumber.value = user.phonenumber
    }

    //viewModel.getExpenseFromFirebase()
    LaunchedEffect(isLoadingPlace) {
        if (location != null) {
            place.value = getLocation(location.first, location.second)?.address?.village ?: ""
            isLoadingPlace = false
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(viewModel.isRefreshing),
        onRefresh = {
            viewModel.isRefreshing = true
            viewModel.getExpenseFromFirebase()
            viewModel.getAllExpenses()
        }
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                //Get current location: place
                if (location != null) {
                    LaunchedEffect(true) {
                        var address = getLocation(location.first, location.second)
                        if (address != null) {
                            place.value = address.address.village
                        }
                    }
                }


                if (windowSize.width > WindowType.Compact) {
                    OverviewCards(
                        modifier=Modifier.height(200.dp).width(200.dp),
                        expense = String.format("%.2f",(getTotalValue(viewModel))),
                        revenue = String.format("%.2f",(getTotalValue(viewModel) / expenses.size))
                    )

                }else{
                    OverviewCards(
                        modifier = Modifier
                            .weight(2f)
                            .aspectRatio(2f),
                        expense = String.format("%.2f",(getTotalValue(viewModel))),
                        revenue = String.format("%.2f",(getTotalValue(viewModel) / expenses.size))
                    )
                }



                Text(
                    text = "Recent group expenses",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                    , contentAlignment = Alignment.Center
                ) {

                    if (expenses.isNotEmpty()) {
                        if (windowSize.width > WindowType.Compact) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                , verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                // Dividir a lista de itens em grupos de 2 (chunked(2))
                                expenses.chunked(2).forEach { rowItems ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        // Para cada item, criamos um Card
                                        rowItems.forEach { expense ->
                                            ExpenseItem(
                                                expense = expense,
                                                navController, modifier = Modifier.height(140.dp).width(300.dp)
                                            )
                                        }

                                        // Caso a linha tenha menos de 2 itens, adiciona um espaço vazio
                                        if (rowItems.size < 2) {
                                            Spacer(modifier = Modifier.weight(1f)) // Preenche o espaço vazio
                                        }
                                    }
                                }
                            }





                        }else{
                            Column {
                                expenses.forEach { expense ->
                                    ExpenseItem(
                                        expense = expense,
                                        navController, modifier = Modifier.aspectRatio(3f)
                                    )
                                }
                            }
                        }
                    } else {
                        GhostCards()
                    }
                }
            }

            AddExpense(windowSize,
                modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
                onClick = {
                    if (user != null) {
                        if (user.group != "") {
                            viewModel.addingExpense()
                        } else {
                            Toast.makeText(
                                context,
                                "Select a group first!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                })
        }
    }

    if (viewModel.loadingExpenses) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center // Centraliza o conteúdo dentro do Box
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }

    if (viewModel.clickedButton) {
        Box(modifier = Modifier.fillMaxSize()) {

            AddExpenseDialog(
                name = name.value,
                value = value.value,
                place = place.value,
                date = data,
                idUser = currentUser.value,
                homeViewModel = viewModel,
                groupViewModel = groupViewModel,
                onNameChange = { name.value = it },
                onValueChange = { value.value = it },
                onDataChange = { it },
                onClick = {
                    viewModel.addExpense(
                        Expense(
                            name = name.value,
                            idUser = currentUser.value,
                            username = username.value,
                            phonenumber=phonenumber.value,
                            idGroup = viewModel.selectedGroup,
                            lat = lat.value,
                            lng = lng.value,
                            image = "",
                            date = data.value,
                            value = value.value,
                            place = place.value
                        ),
                        context = context
                    )
                }, onDismiss = {
                    viewModel.finishingExpense()
                    navController.navigate(Screens.Home.screen)
                })
        }
    }

}

fun getTotalValue(viewModel: HomeViewModel): Double {
    var total = 0.0
    viewModel.expenseData.value?.forEach { expense ->
        total += expense.value.toDouble()
    }
    return total
}

@Composable
fun ExpenseItem(expense: Expense, navController: NavController,modifier: Modifier) {
    ElevatedCard(
        modifier = modifier
            .padding(8.dp)
            .clickable { navController.navigate("expenseDetail/${expense.id}") },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Text(
            text = expense.username,
            fontWeight = FontWeight.Light,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(6.dp)
        )
        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Linha principal com o valor e o nome
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Valor
                Text(
                    text = "  - ${expense.value} €",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = Color.Red,
                    )
                // Nome
                Text(
                    text = expense.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Top)
                )
            }
            // Data na parte inferior
            Text(
                text = expense.date,
                fontWeight = FontWeight.Light,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.End)
            )

        }
    }

}


@Composable
fun GhostCards() {
    Column {
        repeat(5) {
            GhostItem()
        }
    }


}


@Composable
fun GhostItem() {
    val animationSpec: AnimationSpec<Float> = infiniteRepeatable(
        tween(
            durationMillis = 1000,
            easing = LinearEasing
        )
    )
    val alpha = remember { Animatable(0.1f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 0.2f,
            animationSpec = animationSpec
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = Color.LightGray.copy(alpha = alpha.value),
        shape = RoundedCornerShape(8.dp)
    ) {
        Spacer(
            modifier = Modifier
                .height(100.dp)
                .padding(16.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    name: String,
    place: String,
    value: String,
    idUser: String,
    date: MutableState<String>,
    homeViewModel: HomeViewModel,
    groupViewModel: GroupViewModel,
    onNameChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onDataChange: (String) -> Unit,
    onClick: (String) -> Unit,
    onDismiss: () -> Unit

) {

    var groupList = groupViewModel.groupData.value
    var isExpanded by remember { mutableStateOf(false) }
    var selectText by remember { mutableStateOf(groupList?.get(0)) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.secondaryContainer, // Ensure this is not transparent
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    val context = LocalContext.current
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Register expense",
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(fontSize = 40.sp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            label = { Text(text = "Name") },
                            value = name,
                            onValueChange = onNameChange
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedTextField(
                            label = { Text(text = "Place") },
                            value = place,
                            onValueChange = onNameChange,
                            readOnly = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        CustomDatePicker(date, onDataChange)

                        Spacer(modifier = Modifier.height(20.dp))


                        //Selecionar grupo
                        ExposedDropdownMenuBox(
                            expanded = isExpanded,
                            onExpandedChange = { isExpanded = !isExpanded }
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.menuAnchor(),
                                value = selectText?.name ?: "",
                                onValueChange = {
                                    homeViewModel.selectedGroup = selectText?.name ?: ""
                                },
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = isExpanded
                                    )
                                }
                            )
                            ExposedDropdownMenu(
                                expanded = isExpanded,
                                onDismissRequest = { isExpanded = false }) {
                                groupList?.forEachIndexed { index, text ->
                                    DropdownMenuItem(
                                        text = { Text(text = text.name) },
                                        onClick = {
                                            selectText = groupList[index]
                                            isExpanded = false
                                            homeViewModel.selectedGroup =
                                                selectText?.name ?: ""
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedTextField(
                            label = { Text(text = "Value") },
                            value = value,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal
                            ),
                            onValueChange = onValueChange
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        homeViewModel.selectedImage = SinglePhotoPicker()

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                            Button(
                                onClick = {
                                    if (name.isNotBlank()) {
                                        onClick(name)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Please enter an local",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                shape = RoundedCornerShape(50.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Text(text = "Register expense")
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { onDismiss() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Leave"
                        )
                        Text(text = "Leave")

                    }
                }
            }
        }
    }
}


@Composable
fun shakeToRefresh(context: Context,viewModel: HomeViewModel){

    // on below line we are creating a variable for sensor manager and initializing it.
    val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val acceleromentSensor  = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    var shakeDetected by remember { mutableStateOf(false) }
    var lastUpdateTime by remember { mutableStateOf(System.currentTimeMillis()) }

    // Listener para detectar o shake
    val shakeSensorListener = remember {
        object : SensorEventListener {
            private val shakeThreshold = 12f // Definir o valor mínimo de aceleração para considerar "shake"
            private var lastShakeTime = 0L

            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat() - SensorManager.GRAVITY_EARTH
                    if (acceleration > shakeThreshold) {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastShakeTime > 1000) { // Evitar shakes consecutivos em menos de 1 segundo
                            lastShakeTime = currentTime
                            shakeDetected = true
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Não precisa fazer nada aqui
            }
        }
    }

    // Registra o listener do shake
    DisposableEffect(Unit) {
        sensorManager.registerListener(shakeSensorListener, acceleromentSensor, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(shakeSensorListener)
        }
    }

    // Refresh a página ao detectar o shake
    if (shakeDetected) {
        LaunchedEffect(shakeDetected) {
            viewModel.isRefreshing = true
            lastUpdateTime = System.currentTimeMillis()
            shakeDetected = false
        }
        Toast.makeText(
            context,
            "Refreshed!",
            Toast.LENGTH_SHORT
        ).show()
    }


    val formattedDate = remember(lastUpdateTime) {
        val sdf = SimpleDateFormat("dd 'de' MMMM 'de' yyyy, HH:mm:ss", Locale("pt", "PT"))
        sdf.format(Date(lastUpdateTime))
    }
    // Layout da tela Home (pode ser customizado)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = "Last update: ${formattedDate}",
            color = Color.White

        )
    }





}