package com.example.torem.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.torem.R
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