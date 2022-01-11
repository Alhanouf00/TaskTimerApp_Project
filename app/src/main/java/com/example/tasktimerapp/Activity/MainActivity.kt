package com.example.tasktimerapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.CompoundButtonCompat
import androidx.lifecycle.ViewModelProvider
import com.example.tasktimerapp.Adapter.IconAdapter
import com.example.tasktimerapp.R
import com.example.tasktimerapp.TaskViewModel
import com.example.tasktimerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.startBtn.setOnClickListener {
            animation(binding.startBtn)
            intent = Intent(applicationContext, CategoryActivity::class.java)
            startActivity(intent)
        }
    }
    private fun animation(btn: AppCompatButton) {
        val anim = AnimationUtils.loadAnimation(this, R.anim.start_btn_animation)
        btn.startAnimation(anim)
    }
}