package com.example.escmu.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    val name:String,
    val date: String,
    val place:String,
    var image:String,
    val value:String,
    val username:String,
    val idUser:String,
    val idGroup:String
)