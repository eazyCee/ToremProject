package com.example.torem.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TravelPlan(
        var name:String,
        var userID:String,
        var description:String,
        var photo:String,
        var review:String,

        var location1:String,
        var transport1:String,
        var distance1:String,
        var time1:String,

        var location2:String,
        var transport2:String,
        var distance2:String,
        var time2: String,

        var location3:String,
):Parcelable
