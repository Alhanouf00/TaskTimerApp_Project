package com.example.tasktimerapp.Activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasktimerapp.Adapter.IconAdapter
import com.example.tasktimerapp.Adapter.TaskAdapter
import com.example.tasktimerapp.R
import com.example.tasktimerapp.TaskViewModel
import com.example.tasktimerapp.database.Category
import com.example.tasktimerapp.databinding.ActivityTasksBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class TasksActivity : AppCompatActivity() {
    lateinit var binding: ActivityTasksBinding
    lateinit var recyclerAdapter: TaskAdapter
    lateinit var viewModel: TaskViewModel
    lateinit var holdCategory: Category
    var holdTimer = "0"
    var totalTimee: Long = 0

    private val navigasjonen = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_task -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_house -> {
                val intent = Intent(this@TasksActivity, MainActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_section -> {
                val intent = Intent(this@TasksActivity, CategoryActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }

        }
        false

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnNavigationItemSelectedListener(navigasjonen)

        binding.apply {
            //bottom navigation setting
            bottomNav.setOnNavigationItemSelectedListener(navigasjonen)
            bottomNav.menu.findItem(R.id.ic_task).setChecked(true)

            holdCategory = intent.getSerializableExtra("catObject") as Category


            categoryNameTV.text = holdCategory.categoryName

            //adapter setting
            recyclerAdapter = TaskAdapter(this@TasksActivity)
            tasksRv.adapter = recyclerAdapter
            tasksRv.layoutManager = LinearLayoutManager(this@TasksActivity)

            startBtn.setOnClickListener { showAddTaskAlert() }

            val categName = binding.categoryNameTV.text.toString()
            viewModel = ViewModelProvider(this@TasksActivity).get(TaskViewModel::class.java)
            viewModel.fetchCategoriesTasks(categName).observe(this@TasksActivity, { tasks ->
                recyclerAdapter.displayTasks(tasks[0].tasks)
            })
        }
    }

    private fun showAddTaskAlert() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.add_task_alert)

        val addBtn = dialog.findViewById(R.id.addBtn) as Button
        val titleET = dialog.findViewById(R.id.titleET) as? EditText
        val descriptionET = dialog.findViewById(R.id.descriptionET) as? EditText
        val categoryN = binding.categoryNameTV.text.toString()

        addBtn.setOnClickListener {

            if (titleET?.text.toString() != "" && descriptionET?.text.toString() != "") {
                viewModel.addTask(
                    titleET!!.text.toString(),
                    descriptionET!!.text.toString(),
                    " 00:00",
                    categoryN
                )
                Toast.makeText(this@TasksActivity, "Task added successfully", Toast.LENGTH_SHORT)
                    .show()
            } else
                Toast.makeText(this@TasksActivity, "All fields requirement", Toast.LENGTH_SHORT)
                    .show()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun startTimer(run: Boolean) {
        var totalTime: Long? = null

        if (run == true) {
            binding.bigTimerTV.base = SystemClock.elapsedRealtime()
            binding.bigTimerTV.start()
        } else {
            binding.bigTimerTV.stop()
            totalTime = SystemClock.elapsedRealtime() - binding.bigTimerTV.base
            totalTimee = totalTime + totalTimee
            holdTimer = binding.bigTimerTV.text.toString()
            val seconds = totalTimee / 1000 % 60
            holdCategory.totalTime = seconds.toFloat()
            viewModel.editCategory(holdCategory)
        }
    }
}