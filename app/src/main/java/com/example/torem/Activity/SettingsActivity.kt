package com.example.torem.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.torem.R
import com.example.torem.databinding.ActivitySettingsBinding
import com.example.torem.front.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton.setOnClickListener(this)
        binding.logoutButton.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.imageButton->{
                    onBackPressed()
                }
                R.id.logoutButton->{
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }
    }
}