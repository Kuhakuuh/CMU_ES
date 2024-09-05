package com.example.escmu.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper{
    //val base_url="https://nominatim.openstreetmap.org/"
    val retrofit = Retrofit.Builder()
        .baseUrl("https://nominatim.openstreetmap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val locationApi = retrofit.create(LocationApi::class.java)

    suspend fun getLocation(latitude: Double, longitude: Double): LocationResponse? {
        return try {
            val response = locationApi.getLocation(latitude = latitude, longitude = longitude)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}