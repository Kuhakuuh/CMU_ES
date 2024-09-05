package com.example.escmu.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@Composable
fun GetCurrentLocation(): Pair<Double, Double>? {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var locationInfo by remember {
        mutableStateOf<Pair<Double, Double>?>(null)
    }

    PermissionBox(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        requiredPermissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION)
    ) {

        scope.launch(Dispatchers.IO) {
            val priority = Priority.PRIORITY_HIGH_ACCURACY
            val result = locationClient.getCurrentLocation(
                priority,
                CancellationTokenSource().token,
            ).await()
            result?.let { fetchedLocation ->
                locationInfo = Pair(fetchedLocation.latitude, fetchedLocation.longitude)
            }
        }
    }

    return locationInfo
}

@SuppressLint("MissingPermission")
suspend fun getLocationCurrent(context : Context

): Pair<Double, Double>? {


    val  locationClient  =   LocationServices.getFusedLocationProviderClient(context)


    val priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    try {
        val locationResult = locationClient.getCurrentLocation(priority, null)
        val location = locationResult.await()
        return Pair(location.latitude, location.longitude)
    } catch (e: Exception) {
        // Lidar com exceções ao obter a localização
        return null
    }
}


fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}
