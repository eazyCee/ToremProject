package com.example.torem.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Places(
        var name:String?="",
        var placeId:String?="",
        var location:String?="",
        var description:String?="",
        var photo:String?="",
        var review:String?="",
        var reviewer:String?="",
        var source:String?="",

        var event1:String?="",
        var event2:String?="",
        var event3:String?="",

        var eDate1:String?="",
        var eDate2:String?="",
        var eDate3:String?="",

        var eTime1:String?="",
        var eTime2:String?="",
        var eTime3:String?="",

):Parcelable
