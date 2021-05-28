package com.example.torem.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.torem.R
import com.example.torem.data.Places
import com.example.torem.databinding.ActivityDetailBinding
import com.example.torem.databinding.HomeFragmentBinding
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.firestore.FirebaseFirestore
import com.google.protobuf.ApiProto
import com.jaeger.library.StatusBarUtil


class DetailActivity:AppCompatActivity() {
    private lateinit var placesClient: PlacesClient
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        StatusBarUtil.setTransparent(this)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        binding.backButton.setOnClickListener {
            finish()
        }
        initPlaces()
        val extras = intent.extras
        if (extras == null) {
            Log.d("Detail", "extras: null")
        } else {
            val id = extras.getString("id")
            getPlaceCover(id)
        }
    }


    private fun initPlaces(){
        val apiKey="AIzaSyCTLtGlWgAZngkRlmTX4nfJe7dcHrCNXbU"
        com.google.android.libraries.places.api.Places.initialize(applicationContext,apiKey)
        placesClient =com.google.android.libraries.places.api.Places.createClient(this)
    }

    private fun getPlaceCover(placeId:String?) {
        val placeFields =  listOf(Place.Field.NAME, Place.Field.ADDRESS,Place.Field.RATING,Place.Field.PHONE_NUMBER, Place.Field.PHOTO_METADATAS)
        val placeRequest = FetchPlaceRequest.newInstance(placeId!!,placeFields)

        placesClient.fetchPlace(placeRequest)
            .addOnSuccessListener { fetchPlaceResponse->
                val place = fetchPlaceResponse.place
                //get Name
                binding.dpName.text= place.name
                //get Phone
                if (place.phoneNumber!=null){
                binding.dpContact.text= place.phoneNumber
                }else{binding.dpContact.text ="Not Available"}
                //get review
                val rating = place.rating
                val string = rating.toString()
                binding.dpReview.text= string
                //get Location
                binding.dpLocation.text = place.address
                //get Photo
                val photoMetadata=place.photoMetadatas!![0]
                val photoRequest = FetchPhotoRequest.builder(photoMetadata).build()
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPlaceResponse->
                        val bitmap = fetchPlaceResponse.bitmap
                        binding.dpPhoto.setImageBitmap(bitmap)
                    }
            }
    }

}