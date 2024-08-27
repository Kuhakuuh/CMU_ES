package com.example.escmu.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    val date: String,
    val place:String,
    val image:String,
    val value:Int,
    val idUser:String,
    val idGroup:String
)