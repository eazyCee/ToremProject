package com.example.torem.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val id: String = "",
    val name: String = "",
    val username: String="",
    val email: String = "",
    val image: String = "",
    val desc: String = "",
    val profileCompleted: Boolean = false
    ): Parcelable