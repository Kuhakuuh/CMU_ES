package com.example.escmu.retrofit

data class LocationResponse(
    val place_id: String,
    val licence: String,
    val osm_type: String,
    val osm_id: String,
    val lat: String,
    val lon: String,
    val place_rank: String,
    val category: String,
    val type: String,
    val importance: String,
    val addresstype: String,
    val display_name: String,
    val name: String,
    val address: Address,
    val boundingbox: List<String>
)

data class Address(
    val road: String,
    val village: String,
    val state_district: String,
    val state: String,
    val postcode: String,
    val country: String,
    val country_code: String
)