package com.example.tasktimerapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.tasktimerapp.database.Category
import com.example.tasktimerapp.database.Task
import com.example.tasktimerapp.database.TasksDatabase
import com.example.tasktimerapp.database.relations.CategoryTasksRelationship
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TaskViewModel(application: Application): AndroidViewModel(application) {
    private val tasksDao = TasksDatabase.getDatabase(application).dao()
    private var categoriesTasks: LiveData<List<CategoryTasksRelationship>> =
       tasksDao.getCategoryWithTasks("")

    private var categories: LiveData<List<Category>> = tasksDao.getCategories()

        fun fetchCategoriesTasks(categoryName: String): LiveData<List<CategoryTasksRelationship>> {
            categoriesTasks = tasksDao.getCategoryWithTasks(categoryName)
            return categoriesTasks
        }


    fun fetchCategories():  LiveData<List<Category>> {
        return categories
    }

    fun addCategory(name: String, image: Int,totalTime:Float) {
        CoroutineScope(Dispatchers.IO).launch {
            tasksDao.insertCategory(Category(name, image, totalTime))/////////////////////////////////////////
        }
    }

    fun addTask(title: String, description: String, time: String, cateName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            tasksDao.insertTask(Task(0, title, description, time, cateName))
        }
    }

   fun editCategory(catogryObject:Category) {
       CoroutineScope(Dispatchers.IO).launch {
           tasksDao.updateCategory(catogryObject)
       }
   }

    fun editTask(id: Int, title: String, description: String, time: String, cateName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            tasksDao.updateTask(Task(id, title, description, time, cateName))
        }
    }


//
//    fun deleteCategory(name: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            tasksDao.deleteCategory(Category(name, 0))
//        }
//    }
//
//    fun deleteTask(id: Int) {
//        CoroutineScope(Dispatchers.IO).launch {
//            tasksDao.deleteTask(Task(id, "", "", "", ""))
//        }
//    }
}