package com.example.escmu.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class User(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var email: String,
    var name: String,
    var password: String,

)