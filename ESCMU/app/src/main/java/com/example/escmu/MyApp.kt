package com.example.escmu

import android.app.Application
import com.example.escmu.viewmodels.AppContainer
import com.example.escmu.viewmodels.AppDataContainer
import com.google.firebase.Firebase
import com.google.firebase.initialize


class MyApp:Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)

    }


}