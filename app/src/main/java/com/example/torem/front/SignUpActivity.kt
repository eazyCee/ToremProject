package com.example.torem.front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.torem.Activity.HomeActivity
import com.example.torem.BaseActivity
import com.example.torem.R
import com.example.torem.data.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.example.torem.databinding.ActivitySignUpBinding
import com.example.torem.firestore.FirestoreClass


class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener {
            finish()
        }

        binding.signupButton.setOnClickListener{
            registerUser()
        }
    }

    private fun validateRegisterDetails():Boolean{
        return when{
            TextUtils.isEmpty(binding.inputEmail.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.no_email), true)
                false
            }
            TextUtils.isEmpty(binding.inputName.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.no_name), true)
                false
            }
            TextUtils.isEmpty(binding.inputPassword.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.no_password), true)
                false
            }
            TextUtils.isEmpty(binding.inputVerifyPassword.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.no_conf_password), true)
                false
            }
            binding.inputPassword.text.toString().trim{it<=' '}!= binding.inputVerifyPassword.text.toString().trim{it<=' '}->{
                showErrorSnackBar(resources.getString(R.string.password_no_match), true)
                false
            }
            else->{
         //       showErrorSnackBar("You're in!", false)
                true
            }
        }
    }

    private fun registerUser(){
        if(validateRegisterDetails()){

            showProgressDialog("Please wait")

            val email: String = binding.inputEmail.text.toString().trim{it<= ' '}
            val password: String = binding.inputPassword.text.toString().trim{it<= ' '}
            Log.e("reg", "im here")
            //Create Firebase instance and register user
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(
                            OnCompleteListener<AuthResult>{ task ->
                                Log.e("reg", "listening")
                                //if registration is successful
                                if(task.isSuccessful){
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    Log.e("reg", "Registration successful")

                                    val user = User(
                                        firebaseUser.uid,
                                        binding.inputName.text.toString().trim{it<= ' '},
                                        binding.inputUsername.text.toString().trim{it<=' '},
                                        binding.inputEmail.text.toString().trim{it<= ' '}
                                    )

                                    FirestoreClass().registerUser(this, user)

                                    showErrorSnackBar(
                                            "You're in! Your user id is ${firebaseUser.uid}", false
                                    )
                                }else{
                                    showErrorSnackBar(task.exception!!.message.toString(), true)
                                }
                            }
                    )
                    .addOnFailureListener {
                        hideProgressDialog()
                    }
        }
    }

    fun userRegisComplete(){
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        // Finish the Register Screen
        finish()
    }

}