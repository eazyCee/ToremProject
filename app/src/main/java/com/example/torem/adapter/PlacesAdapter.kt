package com.example.torem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.torem.R
import com.example.torem.data.Places
import com.example.torem.databinding.ItemTravelSpotBinding
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest

class PlacesAdapter : RecyclerView.Adapter<PlacesAdapter.ListViewHolder>() {

    private  lateinit var onItemCLickCallback: OnItemClickCallback
    private val mData = ArrayList<Places>()

    fun setData(items: ArrayList<Places>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemTravelSpotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class ListViewHolder(private val binding:ItemTravelSpotBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(placesItems: Places) {
            with(binding) {
                tsName.text = placesItems.name
                tsLocation.text = placesItems.location
                Glide.with(itemView.context)
                    .load(placesItems.photo)
                    .into(tsImage)
                slide.setOnClickListener{
                    onItemCLickCallback.onItemClicked(placesItems)
                }
            }

        }
    }
    fun setOnItemClickCallback(onItemClickCallback : PlacesAdapter.OnItemClickCallback){
        this.onItemCLickCallback= onItemClickCallback
    }
    interface OnItemClickCallback {
        fun onItemClicked(data:Places)
    }
}

