package com.example.torem.util

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object Utils {
    const val TOREM_PREFS: String = "ToremPrefs"
    const val READ_STORAGE_CODE = 2
    const val PICK_IMAGE_CODE = 1
    const val EXTRA_DETAILS: String = "extra_user_detail"
    const val CURRENT_USERNAME: String = "logged_in_username"
    const val DESC: String = "desc"
    fun showImageChooser(activity: Activity){
        val gallery = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(gallery, PICK_IMAGE_CODE)
    }
}