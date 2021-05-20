package com.example.torem.front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.torem.Activity.HomeActivity
import com.example.torem.BaseActivity
import com.example.torem.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.example.torem.databinding.ActivitySignUpBinding



class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupButton.setOnClickListener{
            registerUser(binding)
        }
    }
    
    private fun validateRegisterDetails(binding: ActivitySignUpBinding):Boolean{
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

    private fun registerUser(binding: ActivitySignUpBinding){
        if(validateRegisterDetails(binding)){

            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = binding.inputEmail.text.toString().trim{it<= ' '}
            val password: String = binding.inputPassword.text.toString().trim{it<= ' '}

            //Create Firebase instance and register user
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(
                            OnCompleteListener<AuthResult>{ task ->

                                hideProgressDialog()

                                //if registration is successful
                                if(task.isSuccessful){
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    showErrorSnackBar(
                                            "You're in! Your user id is ${firebaseUser.uid}", false
                                    )
                                    val intent = Intent(this, HomeActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id",firebaseUser.uid)
                                    intent.putExtra("email", email)
                                    startActivity(intent)
                                    finish()
                                }else{
                                    showErrorSnackBar(task.exception!!.message.toString(), true)
                                }
                            }
                    )
        }
    }


}