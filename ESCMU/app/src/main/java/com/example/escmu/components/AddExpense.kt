package com.example.escmu.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.escmu.WindowSize
import com.example.escmu.WindowType

@Composable
fun AddExpense(
    windowSize: WindowSize,
    modifier: Modifier,
               onClick: () -> Unit) {


    if (windowSize.width > WindowType.Compact) {
        LargeFloatingActionButton(
            modifier = modifier,
            onClick = { onClick() },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, "add expenses button.")
        }


    }else{
        FloatingActionButton(
            modifier = modifier,
            onClick = { onClick() },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, "add expenses button.")
        }

    }








}