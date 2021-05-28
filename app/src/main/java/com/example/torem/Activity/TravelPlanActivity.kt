package com.example.torem.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.example.torem.R
import com.example.torem.databinding.ActivityTravelPlanBinding
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import com.jaeger.library.StatusBarUtil
import okhttp3.*
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
    }

    private fun etSource(idOrigins:String,idDestination:String,mode:String) {
        val url ="https://maps.googleapis.com/maps/api/distancematrix/json?origins=place_id:$idOrigins&destinations=place_id:$idDestination&mode=$mode&key=AIzaSyCTLtGlWgAZngkRlmTX4nfJe7dcHrCNXbU"
        val request = Request.Builder().url(url).build()
        val client=OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body =response.body?.string()
                println(body)
            if (body?.contains("distance")==true && body.contains("duration")){
                val gson = GsonBuilder().create()
                val rows = gson.fromJson(body,Rows::class.java)
                    binding.distances1.text = rows.rows[0].elements[0].distance.text
                    binding.duration1.text = rows.rows[0].elements[0].duration.text
                } else{
                    binding.distances1.text = "not found"
                    binding.duration1.text = "not found"
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TP", "onFailure: failed to executed request")
            }
        })
    }
    private fun etSource2(idOrigins:String,idDestination:String,mode:String) {
        val url ="https://maps.googleapis.com/maps/api/distancematrix/json?origins=place_id:$idOrigins&destinations=place_id:$idDestination&mode=$mode&key=AIzaSyCTLtGlWgAZngkRlmTX4nfJe7dcHrCNXbU"
        val request = Request.Builder().url(url).build()
        val client=OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body =response.body?.string()
                println(body)
                if (body?.contains("distance")==true && body.contains("duration")){
                    val gson = GsonBuilder().create()
                    val rows = gson.fromJson(body,Rows::class.java)
                    binding.distances2.text = rows.rows[0].elements[0].distance.text
                    binding.duration2.text = rows.rows[0].elements[0].duration.text
                } else{
                    binding.distances2.text = "not found"
                    binding.duration2.text = "not found"
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TP", "onFailure: failed to executed request")
            }
        })
    }
    class Rows(val rows:List<Row>)
    class Row(val elements:List<Elements>)
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

                    if (mode=="bicycling"){
                        binding.trans1.setImageResource(R.drawable.unmotor)
                    }
                    if (mode=="driving"){
                        binding.trans1.setImageResource(R.drawable.unmobil)
                    }
                    if (mode=="walking"){
                        binding.trans1.setImageResource(R.drawable.unwalk)
                    }
                    if (mode2=="bicycling"){
                        binding.trans2.setImageResource(R.drawable.unmotor)
                    }
                    if (mode2=="driving"){
                        binding.trans2.setImageResource(R.drawable.unmobil)
                    }
                    if (mode2=="walking"){
                        binding.trans2.setImageResource(R.drawable.unwalk)
                    }

                    Glide.with(this)
                        .load(cover)
                        .into(binding.photoCover)

                    getPlaceCover1(idlocation1)
                    getPlaceCover2(idlocation2)
                    getPlaceCover3(idlocation3)

                    Log.d("TP", "onCreate: $idlocation1")
                    if(idlocation1!="" && idlocation2!="")
                        etSource(idlocation1,idlocation2,mode)

                    if(idlocation2!="" && idlocation3!="")
                        etSource2(idlocation2,idlocation3,mode2)

                    binding.editButton.setOnClickListener{
                        db.collection("TravelPlans").document(id)
                            .delete()
                            .addOnSuccessListener { Log.d("TP", "DocumentSnapshot successfully deleted!") }
                            .addOnFailureListener { e -> Log.w("TP", "Error deleting document", e) }
                            finish()

                    }

                    binding.panel1.setOnClickListener {
                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra("id", idlocation1)
                        startActivity(intent)
                    }
                    binding.panel2.setOnClickListener {
                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra("id", idlocation2)
                        startActivity(intent)
                    }
                    binding.panel3.setOnClickListener {
                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra("id", idlocation3)
                        startActivity(intent)
                    }
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