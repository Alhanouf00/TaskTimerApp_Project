package com.example.tasktimerapp.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.tasktimerapp.database.Category
import com.example.tasktimerapp.database.Task

data class CategoryTasksRelationship(

    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryName",  // The primary key of category table
        entityColumn = "categoryName"  // The column in task table that is the primary key of category table
    )

    val tasks: List<Task>
)
