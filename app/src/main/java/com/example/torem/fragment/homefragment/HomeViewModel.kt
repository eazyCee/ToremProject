package com.example.torem.fragment.homefragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.torem.data.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.ArrayList

class HomeViewModel (application: Application) : AndroidViewModel(application){
    val listPlaces = MutableLiveData<ArrayList<Places>>()
    private lateinit var placesClient: PlacesClient


}