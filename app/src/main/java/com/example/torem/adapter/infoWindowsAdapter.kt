package com.example.torem.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import com.example.torem.Activity.DetailActivity
import com.example.torem.R
import com.example.torem.fragment.searchfragment.Search
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class infoWindowsAdapter(context: Context) :GoogleMap.InfoWindowAdapter{
    private lateinit var mWindow :View
    private lateinit var mContext : Context

    init {
        mContext= context
        mWindow= LayoutInflater.from(context).inflate(R.layout.info_window,null)
    }



    private fun rendowWindowText(marker: Marker,view: View) {

        val title = marker.title
        val name = view.findViewById<TextView>(R.id.name)

        if(!title.equals("")){
            name.setText(title)
        }
        val addresses = marker.snippet
        val snippets =view.findViewById<TextView>(R.id.address)

        if(!addresses.equals("")){
            snippets.setText(addresses)
        }

    }

    override fun getInfoWindow(p0: Marker): View? {
        rendowWindowText(p0,mWindow)
        return mWindow
    }

    override fun getInfoContents(p0: Marker): View? {
        rendowWindowText(p0,mWindow)
        return mWindow
    }
}