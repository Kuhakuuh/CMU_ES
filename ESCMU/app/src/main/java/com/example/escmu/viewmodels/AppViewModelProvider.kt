package com.example.escmu.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.escmu.MyApp

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            UserViewModel(myApplication().container.userRepository)
        }


    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.myApplication(): MyApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApp)
