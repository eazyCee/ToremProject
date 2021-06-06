package com.example.torem.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.torem.R
import com.example.torem.front.GetStarted
import com.example.torem.front.LoginActivity
import com.example.torem.util.Utils
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class SpalshScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh_screen)

        var user = FirebaseAuth.getInstance().currentUser

        val sharedPreferences = getDefaultSharedPreferences(this)

        var isJustInstalled = sharedPreferences.getBoolean("justInstalled", true)


        if(user!= null){
            Handler(Looper.getMainLooper()).postDelayed( {
                startActivity(Intent (this, HomeActivity::class.java))
                finish()
            },5000)
        }else{
            if(isJustInstalled) {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()

                editor.putBoolean("justInstalled", false)

                editor.apply()

                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, GetStarted::class.java))
                    finish()
                }, 5000)
            }else {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }, 5000)
            }
        }


    }
}