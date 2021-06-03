package com.example.torem.front

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.torem.R
import com.example.torem.databinding.ActivityGetStartedBinding

class GetStarted : AppCompatActivity() {
    private lateinit var binding: ActivityGetStartedBinding
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.getstarted.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}