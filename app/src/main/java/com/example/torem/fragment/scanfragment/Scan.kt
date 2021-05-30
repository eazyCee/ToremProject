package com.example.torem.fragment.scanfragment


import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.example.torem.databinding.ScanFragmentBinding

private const val CAMERA_REQUEST_CODE =101

class Scan : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var binding:ScanFragmentBinding
    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
      binding= ScanFragmentBinding.inflate(layoutInflater,container,false)
        setupPermission()
        codeScanners()
        return binding.root
    }

    private fun codeScanners() {
      codeScanner = CodeScanner(requireContext(),binding.scannerView)
        codeScanner.apply {
            camera =CodeScanner.CAMERA_BACK
            formats=CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode=ScanMode.CONTINUOUS
            isAutoFocusEnabled=true
            isFlashEnabled=false

            decodeCallback = DecodeCallback {
                activity?.runOnUiThread{
                    if (it.text.isNotEmpty()) {
                        binding.title.text = "You've got"
                        binding.body.text = it.text
                    }
                }
            }
            errorCallback= ErrorCallback {
                activity?.runOnUiThread {
                    Log.d("scan", "codeScanners: ${it.message}")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    private fun setupPermission(){
        val permission:Int = ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.CAMERA)
        if (permission!= PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }
    private fun makeRequest(){
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CAMERA_REQUEST_CODE->{
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {Toast.makeText(requireContext(),"You need camera permission to be able to use this app!",Toast.LENGTH_SHORT).show()
                }else{
                    //success
                }
            }
        }
    }
}