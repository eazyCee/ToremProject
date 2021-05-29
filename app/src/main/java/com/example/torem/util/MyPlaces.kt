package com.example.torem.util

import com.google.firebase.firestore.local.LruGarbageCollector

class MyPlaces {
    var html_attributions:Array<String>?=null
    var status:String?=null
    var next_page_token:String?=null
    var results:Array<Results>?=null
}