package com.example.tasktimerapp.Activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasktimerapp.Adapter.CategoryAdapter
import com.example.tasktimerapp.Adapter.IconAdapter
import com.example.tasktimerapp.R
import com.example.tasktimerapp.TaskViewModel
import com.example.tasktimerapp.database.Category
import com.example.tasktimerapp.databinding.ActivityCategoryBinding
import com.example.tasktimerapp.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView

class CategoryActivity : AppCompatActivity() {
    
    lateinit var binding:ActivityCategoryBinding
    lateinit var iconAdapter : IconAdapter
    lateinit var categoryAdapter : CategoryAdapter
    lateinit var viewModel: TaskViewModel

    private lateinit var barChart: BarChart

    private var categoryList : List<Category> = emptyList()

    private val navigasjonen = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_section -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_house -> {
                val intent1 = Intent(this@CategoryActivity, MainActivity::class.java)
                startActivity(intent1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_task -> {
                val intent2 = Intent(this@CategoryActivity, TasksActivity::class.java)
                startActivity(intent2)
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottomNav.setOnNavigationItemSelectedListener(navigasjonen)

        binding.apply {

            //set bottom nav
            bottomNav.setOnNavigationItemSelectedListener(navigasjonen)
            bottomNav.menu.findItem(R.id.ic_section).setChecked(true)

            //initialize  barChart
            this@CategoryActivity.barChart= barChar

            // icon Adapter setting
            iconAdapter = IconAdapter(this@CategoryActivity) // (this)
            iconRv.adapter = iconAdapter
            iconRv.layoutManager = LinearLayoutManager(this@CategoryActivity, LinearLayoutManager.HORIZONTAL, false)

            // category Adapter setting
            categoryAdapter = CategoryAdapter(this@CategoryActivity)
            categoryRv.adapter = categoryAdapter
            categoryRv.layoutManager = GridLayoutManager(this@CategoryActivity,2)

            drawChar()
        }

        viewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        viewModel.fetchCategories().observe(this, {
            categories -> categoryAdapter.setCategoryList(categories)
            //fill the array of char
            categoryList = categories
            drawChar()
        })
    }

    fun showAddAlert(image : Int){

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.add_category_alert)

        val addBtn = dialog.findViewById(R.id.addBtn) as Button
        val catNameET = dialog.findViewById(R.id.catNameET) as EditText

        addBtn.setOnClickListener {
            viewModel.addCategory(catNameET.text.toString(), image,0f)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun goToTasksView(catObject : Category){
        intent = Intent(applicationContext, TasksActivity::class.java)
        intent.putExtra("catObject", catObject)
        startActivity(intent)
    }

    private fun drawChar() {
        initBarChartUI()

        // draw bar chart with dynamic data
        val entries: ArrayList<BarEntry> = ArrayList()

        //fill y axis
        for (i in categoryList.indices) {
            val categ = categoryList[i]
            entries.add(BarEntry(i.toFloat(), categ.totalTime))
        }

        val barDataSet = BarDataSet(entries, "Category")
        barDataSet.setColors(
            Color.parseColor("#BAB0F7"),
            Color.parseColor("#95DEFF"),
            Color.parseColor("#FFBED6"), Color.parseColor("#E4A9FC"),
            Color.parseColor("#42CFF9"),)
        val data = BarData(barDataSet)
        barChart.data = data
        barChart.setBackgroundResource(R.drawable.chart_backg)

        barChart.invalidate()
    }

    private fun initBarChartUI() {
        //hide grid lines
        barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = barChart.xAxis
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)

        //remove right y-axis
        barChart.axisRight.isEnabled = false

        // legend
        barChart.legend.isEnabled = true
        barChart.legend.textSize = 15f

        //remove description label
        barChart.description.isEnabled = false

        //add animation
        barChart.animateY(1300)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f
        xAxis.setTextSize(15f);
    }

    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            Log.d("Main Activity", "getAxisLabel: index $index")
            return if (index < categoryList.size) {
                categoryList[index].categoryName
            } else {
                ""
            }
        }
    }

}