package com.example.torem.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.torem.R
import com.example.torem.front.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SpalshScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh_screen)

        var user = FirebaseAuth.getInstance().currentUser

        if(user!= null){
            Handler(Looper.getMainLooper()).postDelayed( {
                startActivity(Intent (this, HomeActivity::class.java))
                finish()
            },5000)
        }else{
            Handler(Looper.getMainLooper()).postDelayed( {
                startActivity(Intent (this, LoginActivity::class.java))
                finish()
            },5000)
        }


    }
}