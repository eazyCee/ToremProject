package com.example.torem.fragment.homefragment

import android.app.Application
import android.content.Context
import android.util.Log
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

    fun getPlaceCover(placeId:String) {
        val listOfItem = ArrayList<Places>()
        val placeRequest = FetchPlaceRequest.builder(
            placeId!!,
            listOf(
                Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.PHOTO_METADATAS
            )
        ).build()
        placesClient.fetchPlace(placeRequest)
            .addOnSuccessListener { fetchPlaceResponse ->
                val place = fetchPlaceResponse.place
                val places = Places()
                //get Name
                places.name = place.name!!
                //get Location
                places.location = place.address!!
                //get Photo
                val photoMetadata = place.photoMetadatas!![0]
                places.photo = photoMetadata.toString()
                listOfItem.add(places)
            }
        listPlaces.postValue(listOfItem)
    }
}