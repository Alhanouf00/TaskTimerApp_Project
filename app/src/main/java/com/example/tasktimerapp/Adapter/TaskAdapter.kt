package com.example.tasktimerapp.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimerapp.Activity.TasksActivity
import com.example.tasktimerapp.R
import com.example.tasktimerapp.database.Task
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AnimationUtils
import com.example.tasktimerapp.databinding.TaskRecyclerviewBinding

class TaskAdapter(private val activity: TasksActivity) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    var oldHolder: TaskViewHolder? = null

    class TaskViewHolder(val binding: TaskRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var counterBackground = 0
    private var backgroundList: List<Int> =
        listOf(
            R.drawable.light_blue_r,
            R.drawable.pink_r,
            R.drawable.purple_r
        )

    private var tasks: List<Task> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            TaskRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        animation(holder)
        val task = tasks[position]
        var runOrNot = 0

        if (counterBackground >= backgroundList.size)
            counterBackground = 0

        holder.binding.apply {
            //set background of item
            taskLL.setBackgroundResource(backgroundList[counterBackground])
            counterBackground++

            itemTaskTV.text = task.taskTitle
            itemTimerTV.text = "Time ${task.taskTime}"
            descriptionTV.text = task.taskDescription

            startStopBtn.setOnClickListener {
                if (runOrNot == 0) {
                    if (oldHolder != null)
                        stopPreTask(this@TaskAdapter.oldHolder!!)

                    oldHolder = holder//time will run
                    activity.startTimer(true)
                    runOrNot = 1
                    startStopBtn.setBackgroundResource(R.drawable.stop_ic)
                    activity.binding.aboveTaskTV.text = task.taskTitle

                } else if (runOrNot == 1) {

                    if (oldHolder != null)
                        stopPreTask(this@TaskAdapter.oldHolder!!)
                    oldHolder = holder

                    activity.startTimer(false)
                    activity.binding.aboveTaskTV.text = ""
                    runOrNot = 0
                    startStopBtn.setBackgroundResource(R.drawable.check_ic)
                    startStopBtn.isEnabled = false
                    task.taskTime = activity.holdTimer
                    itemTimerTV.text = task.taskTime
                    itemTimerTV.text = "Time ${task.taskTime}"
                    activity.viewModel.editTask(
                        task.taskPK,
                        task.taskTitle,
                        task.taskDescription,
                        activity.holdTimer,
                        activity.binding.categoryNameTV.text.toString()
                    )
                }
            }
            //expand code
            taskCV.setOnClickListener {

                if (expandableLayout.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(taskCV, AutoTransition())
                    expandableLayout.visibility = View.VISIBLE
                    descriptionTV.text = task.taskDescription
                    expandIV.setImageResource(R.drawable.expand_less_ic)

                } else {
                    TransitionManager.beginDelayedTransition(taskCV, AutoTransition())
                    expandableLayout.visibility = View.GONE
                    expandIV.setImageResource(R.drawable.expand_ic)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun stopPreTask(oldHolder: TaskViewHolder) {
        oldHolder.binding.startStopBtn.setBackgroundResource(R.drawable.check_ic)
        oldHolder.binding.itemTimerTV.text = activity.binding.bigTimerTV.text.toString()
    }

    fun displayTasks(userTasks: List<Task>) {
        this.tasks = userTasks
        notifyDataSetChanged()
    }

    private fun animation(holder: TaskAdapter.TaskViewHolder) {
        val anim = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.rv_animation)
        holder.itemView.startAnimation(anim)
    }
}
