package com.example.torem.fragment.homefragment

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.torem.Activity.DetailActivity
import com.example.torem.R
import com.example.torem.adapter.PlacesAdapter
import com.example.torem.data.Places
import com.example.torem.databinding.HomeFragmentBinding
import com.example.torem.databinding.ItemTravelSpotBinding
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.*

class Home : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private lateinit var bindingSpot: ItemTravelSpotBinding
    private lateinit var adapter: PlacesAdapter
    private lateinit var placesClient: PlacesClient
    private lateinit var homeViewModel:HomeViewModel

    companion object {
    }

    private lateinit var viewModel : HomeViewModel

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
       adapter = PlacesAdapter()
       adapter.notifyDataSetChanged()

       binding = HomeFragmentBinding.inflate(inflater,container,false)
       binding.rvTravelSpot.layoutManager = LinearLayoutManager(activity)
       binding.rvTravelSpot.adapter =adapter
        bindingSpot = ItemTravelSpotBinding.inflate(inflater,container,false)

       adapter.setOnItemClickCallback(object: PlacesAdapter.OnItemClickCallback{
           override fun onItemClicked(data: Places) {
               val intent = Intent(context,DetailActivity::class.java)
               intent.putExtra(DetailActivity.EXTRA_PLACES,data)
               startActivity(intent)
           }
       })
        val view :View = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState : Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(HomeViewModel::class.java)
        Log.d("HomeFragment", "onViewCreated: run")
        homeViewModel.getPlaceCover()

        initPlaces()
        showRecyclerList()
    }

    private fun showRecyclerList() {
        binding.rvTravelSpot.layoutManager = LinearLayoutManager(this)
        val listSpotAdapter=PlacesAdapter()
    }

    private fun initPlaces(){
        val apiKey="AIzaSyCTLtGlWgAZngkRlmTX4nfJe7dcHrCNXbU"
        val contextApp =requireActivity().applicationContext
        com.google.android.libraries.places.api.Places.initialize(contextApp,apiKey)
        placesClient =com.google.android.libraries.places.api.Places.createClient(contextApp)
    }
}