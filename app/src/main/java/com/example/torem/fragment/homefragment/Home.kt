package com.example.torem.fragment.homefragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.torem.Activity.DetailActivity
import com.example.torem.Activity.ProfileActivity
import com.example.torem.R
import com.example.torem.adapter.PlacesAdapter
import com.example.torem.adapter.TPAdapter
import com.example.torem.data.Places
import com.example.torem.data.TravelPlan
import com.example.torem.data.User
import com.example.torem.databinding.HomeFragmentBinding
import com.example.torem.databinding.ItemTravelSpotBinding
import com.example.torem.util.Utils
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestoreSettings
import java.util.*
import kotlin.collections.ArrayList

class Home : Fragment(), View.OnClickListener {

    private lateinit var binding: HomeFragmentBinding
    private lateinit var bindingSpot: ItemTravelSpotBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: PlacesAdapter
    private lateinit var tpAdapter: TPAdapter
    private lateinit var uid: String
    private val db = FirebaseFirestore.getInstance()
    private val tsCollection = db.collection("TravelsPlaces").limit(5)
    private val tpCollection = db.collection("TravelPlans")
    var placesAdapter : PlacesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharedPreferences = this.requireActivity().getSharedPreferences(Utils.TOREM_PREFS,
            Context.MODE_PRIVATE)
        val image = sharedPreferences.getString("image", "")
        val username = sharedPreferences.getString(Utils.CURRENT_USERNAME, "")!!

        binding = HomeFragmentBinding.inflate(layoutInflater, container, false)

        if(image!!.isNotEmpty()) {
            Utils.loadPictureFrag(image, binding.profile, requireActivity())
        }
        binding.userName.text = username

        binding.profile.setOnClickListener(this)

        showRecyclerList()
        return binding.root
    }


    private fun showRecyclerList() {
        val query: Query = tsCollection
        val tpQuery: Query = tpCollection
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Places> =
            FirestoreRecyclerOptions.Builder<Places>()
                .setQuery(query, Places::class.java)
                .build();
        val firestoreTpRecyclerOptions: FirestoreRecyclerOptions<TravelPlan> =
                FirestoreRecyclerOptions.Builder<TravelPlan>()
                        .setQuery(tpQuery, TravelPlan::class.java)
                        .build();
        placesAdapter = PlacesAdapter(firestoreRecyclerOptions)
        tpAdapter = TPAdapter(firestoreTpRecyclerOptions)
        binding.rvTravelPlan.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        binding.rvTravelPlan.adapter = tpAdapter
        binding.rvTravelSpot.layoutManager = LinearLayoutManager(context)
        binding.rvTravelSpot.adapter = placesAdapter
    }


    override fun onStart() {
        super.onStart()
        placesAdapter!!.startListening()
        tpAdapter.startListening()
    }


    override fun onDestroy() {
        super.onDestroy()
        placesAdapter!!.stopListening()
        tpAdapter.stopListening()
    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.profile->{
                    val intent = Intent(requireActivity(), ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}