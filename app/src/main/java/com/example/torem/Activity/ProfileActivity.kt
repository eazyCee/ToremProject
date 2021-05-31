package com.example.torem.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.torem.BaseActivity
import com.example.torem.R
import com.example.torem.data.User
import com.example.torem.databinding.ActivityProfileBinding
import com.example.torem.firestore.FirestoreClass
import com.example.torem.util.Utils

class ProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var userDetails: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editButton.setOnClickListener(this)
        binding.settings.setOnClickListener(this)
        binding.imageButton.setOnClickListener(this)
    }

    private fun getUserDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User){
        userDetails = user

        hideProgressDialog()

        binding.username.text = user.username

        Utils.loadPicture(user.image, binding.circleImageView,this)
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.editButton->{
                    startActivity(Intent(this, EditProfileActivity::class.java))
                }
                R.id.imageButton->{
                    onBackPressed()
                }
                R.id.settings->{
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
            }
        }
    }


}