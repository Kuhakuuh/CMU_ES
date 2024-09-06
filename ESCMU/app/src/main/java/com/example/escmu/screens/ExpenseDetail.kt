package com.example.escmu.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.escmu.Screens
import com.example.escmu.components.GetCurrentLocation
import com.example.escmu.database.models.Expense
import com.example.escmu.viewmodels.AppViewModelProvider
import com.example.escmu.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import kotlinx.coroutines.delay


@Composable
fun ExpenseDetail(
    expenseId:String,
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    if (FirebaseAuth.getInstance().currentUser == null){
        navController.navigate(Screens.Login.screen)
    }

    viewModel.getExpenseFromFirebaseById(expenseId)
    val expense = viewModel.inspectedExpense.value
    val location = GetCurrentLocation()


    // Simula a ação de refresh
    LaunchedEffect(viewModel.isLoading) {
        if (viewModel.isLoading) {
            delay(1000)
            viewModel.isLoading = false
        }
    }

    if (viewModel.isLoading){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    var periodIndex by remember {
        mutableStateOf(0)
    }
    val periodLabels = listOf(
        "Details",
        "Map",
    )
    Column {

        Button(onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.CenterHorizontally) ) {
            Icon(
                imageVector = Icons.Sharp.KeyboardArrowLeft,
                contentDescription ="Back" )

        }
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
                if (expense != null) {
                    ExpenseDetails(expense = expense )
                }
            }
            1 -> {
                if (location != null) {
                    Mapa(location)
                }
            }
        }
    }





}

@Composable
fun ExpenseDetails(expense: Expense){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        expense.let {
            OutlinedTextField(
                value = it.name,
                onValueChange = {},
                label = { Text("Name") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = it.idGroup,
                onValueChange = {},
                label = { Text("Group") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = it.date,
                onValueChange = {},
                label = { Text("Data") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = it.value,
                onValueChange = {},
                label = { Text("Valor") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))


            AsyncImage(
                model = it.image,
                contentDescription = "Imagem da despesa",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
            )

            Spacer(modifier = Modifier.height(16.dp))

        }
    }


}



@Composable
fun Mapa(location:Pair<Double,Double>){
    Box(modifier = Modifier.fillMaxSize()){
        MapboxMap(
            Modifier.fillMaxSize(),
            mapViewportState = rememberMapViewportState {
                setCameraOptions {
                    zoom(6.0)
                    center(Point.fromLngLat(location.second, location.first))
                    pitch(0.0)
                    bearing(0.0)
                }
            },
        )
    }
}
