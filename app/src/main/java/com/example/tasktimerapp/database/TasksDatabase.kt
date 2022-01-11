package com.example.tasktimerapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Category::class, Task::class], version = 4, exportSchema = false)
abstract class TasksDatabase: RoomDatabase() {

    abstract fun dao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TasksDatabase? = null

        fun getDatabase(context: Context): TasksDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TasksDatabase::class.java,
                    "tasks_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance

                return instance
            }
        }
    }
}