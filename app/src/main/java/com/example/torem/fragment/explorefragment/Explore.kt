package com.example.torem.fragment.explorefragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.torem.R
import com.example.torem.adapter.PlacesAdapter
import com.example.torem.adapter.TPAdapter
import com.example.torem.data.Places
import com.example.torem.data.TravelPlan
import com.example.torem.databinding.ExploreFragmentBinding
import com.example.torem.databinding.HomeFragmentBinding
import com.example.torem.databinding.ItemTravelSpotBinding
import com.example.torem.fragment.homefragment.HomeViewModel
import com.example.torem.util.Utils
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Explore : Fragment() {

    private lateinit var binding: ExploreFragmentBinding

    private val db = FirebaseFirestore.getInstance()
    private val tsCollection = db.collection("TravelPlans").limit(5)
    var tpAdapter: TPAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ExploreFragmentBinding.inflate(layoutInflater, container, false)
        showRecyclerList()
        return binding.root

    }


    private fun showRecyclerList() {
        val query: Query = tsCollection
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<TravelPlan> =
            FirestoreRecyclerOptions.Builder<TravelPlan>()
                .setQuery(query, TravelPlan::class.java)
                .build();
        tpAdapter = TPAdapter(firestoreRecyclerOptions)
        binding.rvTravelPlan.layoutManager = GridLayoutManager(context,2)
        binding.rvTravelPlan.adapter = tpAdapter
    }

    override fun onStart() {
        super.onStart()
        tpAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        tpAdapter!!.stopListening()
    }
}