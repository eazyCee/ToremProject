package com.example.torem.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.torem.R
import com.example.torem.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    lateinit var activityHomeBinding: ActivityHomeBinding
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        actionBar?.hide()
        refreshApp()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_home,
            R.id.navigation_add,
            R.id.navigation_explore,
            R.id.navigation_search,
            R.id.navigation_scan
        ).build()
        navView.setupWithNavController(navController)

    }

    private fun refreshApp() {
        activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        activityHomeBinding.refreshLayout.setOnRefreshListener {
            Toast.makeText(this, "Page refreshed", Toast.LENGTH_SHORT).show()
            activityHomeBinding.refreshLayout.isRefreshing = false
        }
    }
}