package com.example.tasktimerapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable



@Entity(tableName = "Categories")
data class Category(
    @PrimaryKey(autoGenerate = false)
    val categoryName: String,
    val categoryImage: Int,
    var totalTime: Float
):Serializable
