package com.example.escmu.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {


    @GET("reverse")
    suspend fun getLocation(
        @Query("format") format: String = "jsonv2",
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Response<LocationResponse>



}