package com.example.torem.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.torem.Activity.DetailActivity
import com.example.torem.data.Places
import com.example.torem.databinding.ItemTravelSpotBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.libraries.places.api.model.Place


class PlacesAdapter(options: FirestoreRecyclerOptions<Places>) :
    FirestoreRecyclerAdapter<Places, PlacesAdapter.ListViewHolder>(options) {

     var places : Places?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemTravelSpotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int, model: Places) {
        holder.name.text = model.name
        holder.location.text = model.location
        Glide.with(holder.itemView.context)
            .load(model.photo)
            .into(holder.photo)
        holder.itemView.setOnClickListener {
            val context = holder.name.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("id", model.placeId)
            context.startActivity(intent)
        }
    }
    inner class ListViewHolder(private val binding:ItemTravelSpotBinding) : RecyclerView.ViewHolder(binding.root){
        var name = binding.tsName
        var location = binding.tsLocation
        var photo = binding.tsImage
        var id = binding.toString()
    }
}

