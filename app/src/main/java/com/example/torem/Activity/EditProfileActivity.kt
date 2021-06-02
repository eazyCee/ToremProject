package com.example.torem.Activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.torem.BaseActivity
import com.example.torem.R
import com.example.torem.data.User
import com.example.torem.databinding.ActivityEditProfileBinding
import com.example.torem.firestore.FirestoreClass
import com.example.torem.util.Utils
import java.io.IOException

class EditProfileActivity : BaseActivity(), View.OnClickListener{
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var userDetails: User
    private var selectedImageUri: Uri? = null
    private var userProfileUrl: String? = ""
    private var uid: String? =""
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.e("editedit", "loadeeed page")

        Log.e("editedit", "got user dataaa")
        if(intent.hasExtra(Utils.EXTRA_DETAILS)){
            userDetails = intent.getParcelableExtra(Utils.EXTRA_DETAILS)!!
        }
        uid = userDetails.id
        getUserDetails()

        binding.inputName.isEnabled = true
        binding.inputName.setText(userDetails.name)

        binding.inputUsername.isEnabled = true
        binding.inputUsername.setText(userDetails.username)

        binding.inputDescription.setText(userDetails.desc)

        binding.picture.setOnClickListener(this)
        binding.signupButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v!= null){
            when(v.id){
                R.id.picture->{
                    if(ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                        == PackageManager.PERMISSION_GRANTED
                    ){
                        Utils.showImageChooser(this)
                    }
                    else{
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Utils.READ_STORAGE_CODE
                        )
                    }
                }
                R.id.back->{
                    finish()
                }
                R.id.signupButton->{
                    Log.e("reg", "click successful")
                    showProgressDialog(resources.getString(R.string.please_wait))
                    Log.e("reg", "dialog shown")
                    if(userProfileUrl!=null){
                        FirestoreClass().uploadImageToCloud(this,selectedImageUri)
                    } else{
                        Log.e("reg", "inside if else")
                        updateProfileData()
                        Log.e("reg", "updated")
                    }
                }
            }
        }
    }

    fun updateSuccessful(){
        hideProgressDialog()

        Toast.makeText(
            this,
            "Details uploaded!",
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun getUserDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCurrentUserDetails(this, uid!!)
    }

    fun userDetailsSuccess(user: User){
        userDetails = user

        hideProgressDialog()

        Utils.loadPicture(user.image, binding.picture,this)
    }

    private fun updateProfileData(){
        val userHashmap = HashMap<String, Any>()

        val desc = binding.inputDescription.text.toString()
        val name = binding.inputName.text.toString()
        val username = binding.inputUsername.text.toString()

        if(desc.isNotEmpty()){
            userHashmap[Utils.DESC] = desc
        }

        if(name.isNotEmpty()){
            userHashmap[Utils.NAME] = name
        }

        if(username.isNotEmpty()){
            userHashmap[Utils.USERNAME] = username
        }


        if(userProfileUrl!!.isNotEmpty()){
            userHashmap[Utils.IMAGE] = userProfileUrl!!
        }

        userHashmap[Utils.PROFILE_COMPLETE] = true

        FirestoreClass().setUserDetails(this, userHashmap, uid!!)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Utils.READ_STORAGE_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showErrorSnackBar("The storage permission is granted.", false)
            } else{
                Toast.makeText(
                    this,
                   "DENIED",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            if(requestCode == Utils.PICK_IMAGE_CODE){
                if(data!= null){
                    loadPicture(data.data!!, binding.picture)
                }
            }
        }
    }

    private fun loadPicture(imageUri: Uri, imageView: ImageView){
            selectedImageUri = imageUri
            Utils.loadPicture(imageUri, imageView, this)
    }

    fun imgUploadSuccess(url: String){
//        hideProgressDialog()
        Toast.makeText(
            this,
            "SUCCESS! IMG URL : $url",
            Toast.LENGTH_SHORT
        ).show()

        userProfileUrl = url
        updateProfileData()
    }

}