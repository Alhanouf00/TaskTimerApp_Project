package com.example.tasktimerapp.database

import androidx.room.*
import androidx.lifecycle.LiveData
import com.example.tasktimerapp.database.relations.CategoryTasksRelationship


@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Transaction
    @Query("SELECT * FROM Categories WHERE categoryName = :categName")
    fun getCategoryWithTasks(categName: String): LiveData<List<CategoryTasksRelationship>>

    @Query("SELECT * FROM Categories")
    fun getCategories(): LiveData<List<Category>>

    @Update
    suspend fun updateCategory(category: Category)

    @Update
    suspend fun updateTask(task: Task)

}