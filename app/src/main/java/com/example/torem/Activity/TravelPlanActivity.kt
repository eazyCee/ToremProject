package com.example.torem.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.webkit.WebStorage
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.torem.R
import com.example.torem.adapter.PlacesAdapter
import com.example.torem.data.Places
import com.example.torem.databinding.ActivityDetailBinding
import com.example.torem.databinding.ActivityTravelPlanBinding
import com.example.torem.databinding.AddFragmentBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.auth.User
import com.google.gson.GsonBuilder
import com.jaeger.library.StatusBarUtil
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class TravelPlanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTravelPlanBinding
    private lateinit var placesClient: PlacesClient
    private var idlocation1:String =""
    private var idlocation2:String =""
    private var idlocation3:String =""
    private var mode:String =""
    private var mode2:String =""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_plan)
        binding = ActivityTravelPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        StatusBarUtil.setTransparent(this)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        binding.backButton.setOnClickListener {
            finish()
        }
        initPlaces()
        showData()
        etSource(idlocation1,idlocation2,mode)
    }

    private fun etSource(idOrigins:String,idDestination:String,mode:String) {
        val url ="https://maps.googleapis.com/maps/api/distancematrix/json?origins=place_id:$idOrigins&destinations=place_id:$idDestination&mode=$mode&key=AIzaSyCTLtGlWgAZngkRlmTX4nfJe7dcHrCNXbU"
        val request = Request.Builder().url(url).build()
        val client=OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body =response.body?.string()
                println(body)
                val gson = GsonBuilder().create()
                val rows = gson.fromJson(body,Rows::class.java)
                if (rows.rows.elements.status == "OK") {
                    binding.distances1.text = rows.rows.elements.distance.text
                    binding.distances1.text = rows.rows.elements.duration.text
                } else{
                    binding.distances1.text = "not found"
                    binding.distances1.text = "not found"
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TP", "onFailure: failed to executed request")
            }
        })
    }
    class Rows(val rows:Row)
    class Row(val elements:Elements)
    class Elements(val distance: Distance,val duration:Duration,val status:String)
    class Distance(val text: String,val value:String)
    class Duration(val text: String,val value:String)


    private fun initPlaces(){
        val apiKey="AIzaSyCTLtGlWgAZngkRlmTX4nfJe7dcHrCNXbU"
        com.google.android.libraries.places.api.Places.initialize(applicationContext,apiKey)
        placesClient =com.google.android.libraries.places.api.Places.createClient(this)
    }
    private fun showData() {
        val extras= intent.extras

        if (extras == null) {
            Log.d("fireStore", "extras: null")
        } else {
            val id = extras.getString("documentID").toString()

            val db = FirebaseFirestore.getInstance()
            db.collection("TravelPlans").document(id)
                .get()
                .addOnSuccessListener {
                    binding.title.text = it.getString("nameTP").toString()
                    idlocation1 = it.getString("firstLocation").toString()
                    idlocation2 = it.getString("secondLocation").toString()
                    idlocation3 = it.getString("thirdLocation").toString()
                    val cover = it.getString("cover").toString()
                    binding.editDescription.text = it.getString("descriptionTP").toString()
                    mode = it.getString("mode1").toString()
                    mode2 = it.getString("mode2").toString()

                    Glide.with(this)
                        .load(cover)
                        .into(binding.photoCover)

                    getPlaceCover1(idlocation1)
                    getPlaceCover2(idlocation2)
                    getPlaceCover3(idlocation3)

                }
        }
    }
    private fun getPlaceCover1(placeId:String?) {
        val placeFields =  listOf(
            Place.Field.NAME,Place.Field.PHOTO_METADATAS)
        val placeRequest = FetchPlaceRequest.newInstance(placeId!!,placeFields)

        placesClient.fetchPlace(placeRequest)
            .addOnSuccessListener { fetchPlaceResponse->
                val place = fetchPlaceResponse.place
                //get Name
                binding.location1.text= place.name
                //get Photo
                val photoMetadata=place.photoMetadatas!![0]
                val photoRequest = FetchPhotoRequest.builder(photoMetadata).build()
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPlaceResponse->
                        val bitmap = fetchPlaceResponse.bitmap
                        binding.photo1.setImageBitmap(bitmap)
                    }
            }
    }
    private fun getPlaceCover2(placeId:String?) {
        val placeFields =  listOf(
            Place.Field.NAME,Place.Field.PHOTO_METADATAS)
        val placeRequest = FetchPlaceRequest.newInstance(placeId!!,placeFields)

        placesClient.fetchPlace(placeRequest)
            .addOnSuccessListener { fetchPlaceResponse->
                val place = fetchPlaceResponse.place
                //get Name
                binding.location2.text= place.name
                //get Photo
                val photoMetadata=place.photoMetadatas!![0]
                val photoRequest = FetchPhotoRequest.builder(photoMetadata).build()
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPlaceResponse->
                        val bitmap = fetchPlaceResponse.bitmap
                        binding.photo2.setImageBitmap(bitmap)
                    }
            }
    }
    private fun getPlaceCover3(placeId:String?) {
        val placeFields =  listOf(
            Place.Field.NAME,Place.Field.PHOTO_METADATAS)
        val placeRequest = FetchPlaceRequest.newInstance(placeId!!,placeFields)

        placesClient.fetchPlace(placeRequest)
            .addOnSuccessListener { fetchPlaceResponse->
                val place = fetchPlaceResponse.place
                //get Name
                binding.location3.text= place.name
                //get Photo
                val photoMetadata=place.photoMetadatas!![0]
                val photoRequest = FetchPhotoRequest.builder(photoMetadata).build()
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPlaceResponse->
                        val bitmap = fetchPlaceResponse.bitmap
                        binding.photo3.setImageBitmap(bitmap)
                    }
            }
    }
}