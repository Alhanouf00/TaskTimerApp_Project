package com.example.tasktimerapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskPK: Int,
    val taskTitle: String,
    val taskDescription: String,
    var taskTime: String,
    val categoryName: String
)
