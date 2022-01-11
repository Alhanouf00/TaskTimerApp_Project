package com.example.tasktimerapp.Adapter


import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimerapp.Activity.CategoryActivity
import com.example.tasktimerapp.R
import com.example.tasktimerapp.database.Category
import com.example.tasktimerapp.databinding.CategoryRecyclerviewBinding

class CategoryAdapter(private val activity: CategoryActivity): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(val binding: CategoryRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var counterBackground = 0
    private var backgroundList: List<Int> =
        listOf(
            R.drawable.light_blue_s,
            R.drawable.pink_s,
            R.drawable.purple_s,
            R.drawable.dark_blue_s
        )

    private var categoryList: List<Category> = emptyList()

    fun setCategoryList(userCategories: List<Category>) {
        this.categoryList = userCategories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {


        val currentCategory = categoryList[position]

        holder.binding.apply {
            // item animation
            animation(holder)
            //set background of item
            if (counterBackground == backgroundList.size)
                counterBackground = 0

            backgLayout.setBackgroundResource(backgroundList[counterBackground])
            counterBackground++

            iconIV.setImageResource(currentCategory.categoryImage)
            catNameTV.text = currentCategory.categoryName

            cardLayout.setOnClickListener {
                activity.goToTasksView(currentCategory)
            }
        }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    private fun animation(holder: CategoryViewHolder) {
        val anim = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.rv_animation)
        holder.itemView.startAnimation(anim)
    }
}