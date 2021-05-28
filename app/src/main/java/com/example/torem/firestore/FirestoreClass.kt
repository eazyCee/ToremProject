package com.example.torem.firestore


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.torem.Activity.EditProfileActivity
import com.example.torem.data.User
import com.example.torem.front.LoginActivity
import com.example.torem.front.SignUpActivity
import com.example.torem.util.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()
    
    fun registerUser(activity: SignUpActivity, userInfo: User){
        mFirestore.collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisComplete()
            }
            .addOnFailureListener{ e->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user",
                    e
                )
            }
    }

    fun getCurrentUserID():String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if(currentUser!=null){
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity){
        mFirestore.collection("users")
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener{document->
                Log.i(activity.javaClass.simpleName,document.toString())

                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(
                    Utils.TOREM_PREFS,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Utils.CURRENT_USERNAME,
                    "${user.username}"
                )
                editor.putString(
                    "user_description",
                    "${user.desc}"
                )
                editor.putString(
                    "user_name",
                    "${user.name}"
                )
                editor.apply()


                when(activity){
                    is LoginActivity->{
                        activity.userLoggedInSuccessfully(user)
                    }
                }
            }
    }

    fun setUserDetails(activity: Activity, userHashMap: HashMap<String, Any>){
        mFirestore.collection("users").document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is EditProfileActivity->{
                        activity.updateSuccessful()
                    }

                }
            }
            .addOnFailureListener{e->
                when(activity){
                    is EditProfileActivity->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating user details",
                    e
                )
            }
    }
}