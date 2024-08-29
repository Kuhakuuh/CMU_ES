package com.example.escmu.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
@Entity("groups")
data class Group(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    val name:String,
    val totalValue:Double,
    val idUser:String,
)