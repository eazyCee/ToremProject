package com.example.torem.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.torem.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        actionBar?.hide()
    }
}