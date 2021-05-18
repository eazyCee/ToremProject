package com.example.torem.front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.torem.BaseActivity
import com.example.torem.R
import com.example.torem.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {
    val binding = ActivityLoginBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signUp.setOnClickListener(this)
        binding.loginButton.setOnClickListener(this)
    }

    override fun onClick(v: View?){
        if(v!=null){
            when(v.id){
                R.id.loginButton->{
                    loginUser()
                }
                R.id.signUp->{
                    val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean{
        return when {
            TextUtils.isEmpty(binding.inputEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.no_email), true)
                false
            }
            TextUtils.isEmpty(binding.inputPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.no_name), true)
                false
            }

            else->{
                showErrorSnackBar("Valid!",false)
                true
            }
        }
    }

    private fun loginUser(){
        if(validateLoginDetails()){
            showProgressDialog(resources.getString(R.string.please_wait))

            val email = binding.inputEmail.text.toString().trim{it<= ' '}
            val password = binding.inputPassword.text.toString().trim{it<=' '}

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task->
                        hideProgressDialog()

                        if(task.isSuccessful){
                            showErrorSnackBar("You are logged in.", false)
                        } else{
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }

            }
        }
    }
}