package com.example.torem.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.torem.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var activityHomeBinding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        refreshApp()

    }
    private fun refreshApp(){
        activityHomeBinding=ActivityHomeBinding.inflate(layoutInflater)
        activityHomeBinding.refreshLayout.setOnRefreshListener {
            Toast.makeText(this,"Page refreshed",Toast.LENGTH_SHORT).show()
            activityHomeBinding.refreshLayout.isRefreshing=false
        }
    }
}