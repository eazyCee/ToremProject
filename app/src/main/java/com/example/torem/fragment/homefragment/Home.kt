package com.example.torem.fragment.homefragment

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
import com.example.torem.adapter.PlacesAdapter
import com.example.torem.data.Places
import com.example.torem.databinding.HomeFragmentBinding
import com.example.torem.databinding.ItemTravelSpotBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestoreSettings
import java.util.*
import kotlin.collections.ArrayList

class Home : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private lateinit var bindingSpot: ItemTravelSpotBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: PlacesAdapter
    private val db = FirebaseFirestore.getInstance()
    private val tsCollection: CollectionReference = db.collection("TravelsPlaces")
    var placesAdapter : PlacesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(layoutInflater, container, false)
        binding.rvTravelPlan.layoutManager = LinearLayoutManager(context)
        showRecyclerList()
        return binding.root

    }


    private fun showRecyclerList() {
        val query: Query = tsCollection
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Places> =
            FirestoreRecyclerOptions.Builder<Places>()
                .setQuery(query, Places::class.java)
                .build();
        placesAdapter = PlacesAdapter(firestoreRecyclerOptions)
        binding.rvTravelSpot.layoutManager = LinearLayoutManager(context)
        binding.rvTravelSpot.adapter = placesAdapter
    }

    override fun onStart() {
        super.onStart()
        placesAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        placesAdapter!!.stopListening()
    }
}