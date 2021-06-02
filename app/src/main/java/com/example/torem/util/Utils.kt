package com.example.torem.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.torem.R
import java.io.IOException

object Utils {
    const val NAME: String = "name"
    const val USERNAME: String = "username"
    const val IMAGE: String = "image"
    const val PROFILE_COMPLETE: String = "profileCompleted"
    const val TOREM_PREFS: String = "ToremPrefs"
    const val READ_STORAGE_CODE = 2
    const val PICK_IMAGE_CODE = 1
    const val EXTRA_DETAILS: String = "extra_user_detail"
    const val CURRENT_USERNAME: String = "logged_in_username"
    const val DESC: String = "desc"
    fun showImageChooser(activity: Activity) {
        val gallery = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(gallery, PICK_IMAGE_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

    fun loadPicture(image: Any, imageView: ImageView, context: Context) {
    try{
        Glide.with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.userprof)
                .into(imageView)
    } catch(e: IOException)
    {
        e.printStackTrace()
    }
}
}