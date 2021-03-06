package com.example.torem.fragment.addfragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.opengl.Visibility
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.torem.Activity.TravelPlanActivity
import com.example.torem.R
import com.example.torem.databinding.AddFragmentBinding
import com.example.torem.firestore.FirestoreClass
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.Utils
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Add : Fragment() {
    private lateinit var placesClient: PlacesClient
    private var location :String ="String"
    private var name:String ="String"
    private var name2:String ="String"
    private var name3:String ="String"
    private var addresses:String ="String"
    private var addresses2:String ="String"
    private var addresses3:String ="String"
    private var location2 :String ="String"
    private var location3 :String ="String"
    private var cover: String=""
    private var nameTp:String ="Sring"
    private var uri: Uri = Uri.EMPTY
    private lateinit var binding: AddFragmentBinding
    private var mode:String=""
    private var mode2:String=""
    private val mAuth= FirebaseAuth.getInstance()
    private var checked:String = "No"
    var hashMap : HashMap<Int, String> = HashMap<Int, String> ()
    private  var nameuser:String = ""
    
    private val temp:String = System.currentTimeMillis().toString()


    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        binding = AddFragmentBinding.inflate(layoutInflater,container,false)
        customize()
        initPlaces()
        getCurrentUserName()
        autoPlaces()
        autoPlaces2()
        autoPlaces3()
        binding.imageButton.setOnClickListener{
            val intent =Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"Choose Picture"),111)
            binding.apply.visibility = View.VISIBLE
        }
        binding.apply.setOnClickListener{
                uploadFile()
            binding.apply.visibility = View.GONE
        }
        binding.radio1.setOnCheckedChangeListener { group, checkedId ->
            val rb = view?.findViewById<RadioButton>(checkedId)
            mode=rb?.text.toString()

        }
        binding.radio2.setOnCheckedChangeListener { group, checkedId ->
            val rb = view?.findViewById<RadioButton>(checkedId)
            mode2=rb?.text.toString()

        }
        binding.createButton.setOnClickListener{
            binding.progressBar.visibility = View.VISIBLE
            val nameTP = binding.editTitle.text.toString()
            if (nameTP.isEmpty()){
                binding.editTitle.error = "Please Enter a title!"
            }
            val userID = FirestoreClass().getCurrentUserID()
            val userName = nameuser
            val descriptionTP = binding.editDescription.text.toString()
            val firstLocation = location
            val secondLocation = location2
            val thirdLocation = location3
            val covers = hashMap.get(1).toString()
            val mode1 = mode
            val mode2 = mode2
            saveFireStore(firstLocation,secondLocation,thirdLocation,covers,nameTP,descriptionTP,mode1,mode2,userID,userName)
        }
        return binding.root
    }

    private fun uploadFile() {
        if (uri != null) {
            val imageRef: StorageReference = FirebaseStorage.getInstance().reference.child("cover/"+
                    "tp_Image" + System.currentTimeMillis().toString())
            imageRef.putFile(uri).addOnSuccessListener {
                it.metadata!!.reference!!.downloadUrl.addOnSuccessListener { Uri->
                    Toast.makeText(requireContext(),"Image Applied",Toast.LENGTH_SHORT).show()
                    val uri = Uri.toString()
                    url(uri)
                }
            }
        }
    }
    private fun url(uri:String){
        hashMap.put(1,uri)
        Log.d("add", "url: $uri")
    }

    fun saveFireStore(FirstLocation:String,SecondLocation:String,Thirdlocation:String,Cover:String,Title:String,Description:String,Mode:String,Mode2:String,UserID:String,userName:String) {
        val db = FirebaseFirestore.getInstance()
        val travelPlan: MutableMap<String, Any> = HashMap()
        travelPlan["firstLocation"] = FirstLocation
        travelPlan["secondLocation"] = SecondLocation
        travelPlan["thirdLocation"] = Thirdlocation
        travelPlan["cover"] = Cover
        travelPlan["nameTP"] = Title
        travelPlan["descriptionTP"] = Description
        travelPlan["mode1"] = Mode
        travelPlan["mode2"] = Mode2
        travelPlan["userID"] = UserID
        travelPlan["userName"] = userName
        var temp = Title + System.currentTimeMillis()
        travelPlan["tpId"] = temp

        db.collection("TravelPlans").document(temp)
            .set(travelPlan)
            .addOnSuccessListener {
                if(Title.isEmpty()){
                    binding.progressBar.visibility = View.GONE
                    binding.save.visibility = View.GONE
                    binding.done.setImageResource(R.drawable.ic_baseline_refresh_24)
                    binding.done.visibility = View.VISIBLE
                }else{
                binding.progressBar.visibility = View.GONE
                binding.save.visibility = View.GONE
                binding.done.visibility = View.VISIBLE

                val intent = Intent(context, TravelPlanActivity::class.java)
                intent.putExtra("documentID",temp)
                context?.startActivity(intent)}

            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                binding.save.text = "Failed"
            }
    }
    fun getCurrentUserName(){
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        db.collection("users")
            .document(currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                nameuser = it.getString("username").toString()
                Log.d("FC", "getCurrentUserName: $nameuser")

            }

    }

    fun initPlaces () {
        val apiKey="AIzaSyCTLtGlWgAZngkRlmTX4nfJe7dcHrCNXbU"
        Places.initialize(requireActivity().application, apiKey)
        placesClient =Places.createClient(requireContext())
    }
    fun autoPlaces(){
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                location = place.id.toString()
                name = place.name.toString()
                addresses =place.address.toString()

                Log.i("add", "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {
                Log.i("add", "An error occurred: $status")
            }
        })
    }
    fun autoPlaces2(){
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment2) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                location2 = place.id.toString()
                name2 = place.name.toString()
                addresses2 =place.address.toString()
                Log.i("add", "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {
                Log.i("add", "An error occurred: $status")
            }
        })
    }
    fun autoPlaces3(){
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment3) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                location3 = place.id.toString()
                location3 = place.id.toString()
                name3 = place.name.toString()
                addresses3 =place.address.toString()
                Log.i("add", "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {
                Log.i("add", "An error occurred: $status")
            }
        })
    }
    fun customize(){
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
        autocompleteFragment!!.view?.findViewById<EditText>(R.id.places_autocomplete_search_input)?.textSize = 17.0f
        autocompleteFragment.view?.findViewById<EditText>(R.id.places_autocomplete_search_input)?.setTextColor(Color.DKGRAY)

        val autocompleteFragment2 = childFragmentManager.findFragmentById(R.id.autocomplete_fragment2)
        autocompleteFragment2!!.view?.findViewById<EditText>(R.id.places_autocomplete_search_input)?.textSize = 17.0f
        autocompleteFragment2.view?.findViewById<EditText>(R.id.places_autocomplete_search_input)?.setTextColor(Color.DKGRAY)

        val autocompleteFragment3 = childFragmentManager.findFragmentById(R.id.autocomplete_fragment3)
        autocompleteFragment3!!.view?.findViewById<EditText>(R.id.places_autocomplete_search_input)?.textSize = 17.0f
        autocompleteFragment3.view?.findViewById<EditText>(R.id.places_autocomplete_search_input)?.setTextColor(Color.DKGRAY)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==111 && resultCode==Activity.RESULT_OK && data!=null) {
             uri = data.data!!
            val bitmap=MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,uri)
            binding.photo.setImageBitmap(bitmap)
        }
    }
}

