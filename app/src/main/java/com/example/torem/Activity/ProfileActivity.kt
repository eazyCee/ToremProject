package com.example.torem.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.torem.BaseActivity
import com.example.torem.R
import com.example.torem.adapter.TPAdapter
import com.example.torem.data.TravelPlan
import com.example.torem.data.User
import com.example.torem.databinding.ActivityProfileBinding
import com.example.torem.firestore.FirestoreClass
import com.example.torem.util.Utils
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var userDetails: User
    private lateinit var tpList: ArrayList<TravelPlan>
    private lateinit var adapter: TPAdapter
    private lateinit var uid: String
    var tpAdapter: TPAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUserDetails()
        val sharedPreferences = this.getSharedPreferences(Utils.TOREM_PREFS,
                Context.MODE_PRIVATE)
        uid = sharedPreferences.getString("uid", "")!!
        showRecyclerList()
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

        binding.description.text = user.desc

        Utils.loadPicture(user.image, binding.circleImageView,this)
    }

    override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    override fun onDestroy(){
        super.onDestroy()
        adapter.stopListening()
    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.editButton->{
                    val editIntent = Intent(this, EditProfileActivity::class.java)
                    editIntent.putExtra(Utils.EXTRA_DETAILS,  userDetails)
                    startActivity(editIntent)
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

    fun tpFromFirestoreSuccess(tpList: ArrayList<TravelPlan>){
        hideProgressDialog()

        for(i in tpList){
            Log.i("TP",i.nameTP!!)
        }
    }

    private fun getTpFromFirestore(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getTravelPlans(this)
    }

    private fun showRecyclerList() {
        Log.e("recycler","displaying...")
        val tsCollection = FirebaseFirestore.getInstance().collection("TravelPlans").whereEqualTo("userID", uid)
        val query: Query = tsCollection
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<TravelPlan> =
            FirestoreRecyclerOptions.Builder<TravelPlan>()
                .setQuery(query, TravelPlan::class.java)
                .build()
        adapter = TPAdapter(firestoreRecyclerOptions)
        binding.profileTp.layoutManager = GridLayoutManager(this,2)
        binding.profileTp.adapter = adapter
        Log.e("recycler","donezoooooo")
    }
}