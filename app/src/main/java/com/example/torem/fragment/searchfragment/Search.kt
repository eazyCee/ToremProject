package com.example.torem.fragment.searchfragment

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.CaseMap
import android.location.Location
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.torem.Activity.DetailActivity
import com.example.torem.Activity.TravelPlanActivity
import com.example.torem.R
import com.example.torem.Remote.IGoogleAPIService
import com.example.torem.databinding.ActivityTravelPlanBinding
import com.example.torem.databinding.SearchFragmentBinding
import com.example.torem.util.MyPlaces
import com.example.torem.util.common
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Search : Fragment() {


    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: SearchFragmentBinding
    private lateinit var googleMapsFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap

    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()

    private lateinit var mLastLocation: Location
    private var mMarker: Marker? = null

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    companion object {
        private const val MY_PERMISSION_CODE: Int = 1000
    }

    lateinit var mServices:IGoogleAPIService
    internal lateinit var currentPlace:MyPlaces

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentBinding.inflate(layoutInflater, container, false)
        googleMapsFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        googleMapsFragment.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(p0: GoogleMap) {
                mMap = p0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        mMap.isMyLocationEnabled = true
                    }
                } else {
                    mMap.isMyLocationEnabled = true
                    mMap.uiSettings.isZoomControlsEnabled = true
                }
            }
        })
        mServices=common.googleApiService

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
            buildLocationRequest()
            buildLocationCallBack()

            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext());
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        } else {
            checkLocationPermission()
            buildLocationRequest()
            buildLocationCallBack()

            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext());
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        }
        binding.amusement.setOnClickListener{
            nearByplaces("amusement_park")
        }
        binding.park.setOnClickListener{
            nearByplaces("park")
        }
        binding.museum.setOnClickListener{
            nearByplaces("museum")
        }
        binding.campground.setOnClickListener{
            nearByplaces("campground")
        }
        binding.touristAttraction.setOnClickListener{
            nearByplaces("tourist_attraction")
        }
        return binding.root
    }

    private fun nearByplaces(typePlace: String) {
    mMap.clear()
        val url = getUrl(latitude,longitude,typePlace)

        mServices.getNearbyPlaces(url)
            .enqueue(object: Callback<MyPlaces> {
                override fun onResponse(call: Call<MyPlaces>, response: Response<MyPlaces>) {
                    currentPlace=response.body()!!
                    if (response!!.isSuccessful)
                    {
                        for (i in 0 until response.body()!!.results!!.size)
                        {
                            val markerOptions=MarkerOptions()
                            val googlePlaces =response.body()!!.results!![i]
                            val lat = googlePlaces.geometry!!.location!!.lat
                            val lng = googlePlaces.geometry!!.location!!.lng
                            val placeName = googlePlaces.name
                            val latLng = LatLng(lat,lng)


                            markerOptions.position(latLng)
                            markerOptions.title(placeName)
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            mMap!!.addMarker(markerOptions)
                            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
                        }

                    }
                }

                override fun onFailure(call: Call<MyPlaces>, t: Throwable) {
                   Toast.makeText(activity?.baseContext,""+t.message,Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun getUrl(latitude: Double, longitude: Double, typePlace: String): String {
        val googlePlaceUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude,$longitude")
        googlePlaceUrl.append("&radius=15000")
        googlePlaceUrl.append("&type=$typePlace")
        googlePlaceUrl.append("&key=AIzaSyCTLtGlWgAZngkRlmTX4nfJe7dcHrCNXbU")
        Log.d("URL_DEBUG", googlePlaceUrl.toString())
        return googlePlaceUrl.toString()

    }

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                mLastLocation = p0.locations.get(p0.locations.size - 1)
                if (mMarker != null) {
                    mMarker!!.remove()
                }
                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude
                val latLng = LatLng(latitude, longitude)
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title("Your position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                mMarker = mMap.addMarker(markerOptions)

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
            }
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            else
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            return false
        } else
            return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                        if (checkLocationPermission()) {
                            checkLocationPermission()
                            buildLocationRequest()
                            buildLocationCallBack()

                            fusedLocationProviderClient =
                                LocationServices.getFusedLocationProviderClient(requireContext());
                            fusedLocationProviderClient.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.myLooper()
                            )

                            mMap.isMyLocationEnabled = true
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Permission Denied",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }
    }

    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }
}